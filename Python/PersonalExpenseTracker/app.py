from flask import Flask, render_template, request, jsonify
import datetime
import database

app = Flask(__name__)
app.secret_key = 'your_secret_key'

# Initialize the database before running the application
database.init_db()

@app.route('/')
def index():
    transactions = database.get_transactions(1)  # For demo purposes, hard-coded user_id
    balance = sum(tx[2] for tx in transactions)  # Sum the amounts for balance
    return render_template('index.html', balance=balance, transactions=transactions)

@app.route('/add', methods=['POST'])
def add_transaction():
    try:
        data = request.get_json()  # Get JSON data from the request
        amount = float(data['amount'])
        transaction_type = data['transaction_type']
        category = data['category']
        description = data.get('description', '')

        # Validate amount
        if amount <= 0:
            return jsonify({"error": "Amount must be greater than zero."}), 400

        # Adjust balance based on transaction type
        date = datetime.datetime.now().strftime('%Y-%m-%d %H:%M:%S')
        user_id = 1  # Hardcoded user_id for demo
        
        # Store the transaction in the database
        database.add_transaction(user_id, amount if transaction_type == 'credit' else -amount, 
                                 transaction_type, category, description)

        # Calculate new balance
        balance = sum(tx[2] for tx in database.get_transactions(user_id))  # Recalculate balance

        return jsonify({"new_balance": balance, "date": date})
    except Exception as e:
        return jsonify({"error": str(e)}), 500

if __name__ == '__main__':
    app.run(debug=True)
