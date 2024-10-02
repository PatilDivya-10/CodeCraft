import sqlite3

DATABASE_NAME = 'expense_tracker.db'

def init_db():
    with sqlite3.connect(DATABASE_NAME) as conn:
        cursor = conn.cursor()
        cursor.execute('''
            CREATE TABLE IF NOT EXISTS transactions (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                user_id INTEGER NOT NULL,
                amount REAL NOT NULL,
                transaction_type TEXT NOT NULL,
                category TEXT NOT NULL,
                description TEXT,
                date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
        ''')
        conn.commit()

def add_transaction(user_id, amount, transaction_type, category, description):
    with sqlite3.connect(DATABASE_NAME) as conn:
        cursor = conn.cursor()
        cursor.execute('''
            INSERT INTO transactions (user_id, amount, transaction_type, category, description)
            VALUES (?, ?, ?, ?, ?)
        ''', (user_id, amount, transaction_type, category, description))
        conn.commit()

def get_transactions(user_id):
    with sqlite3.connect(DATABASE_NAME) as conn:
        cursor = conn.cursor()
        cursor.execute('SELECT * FROM transactions WHERE user_id = ?', (user_id,))
        return cursor.fetchall()
