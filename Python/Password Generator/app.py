from flask import Flask, render_template, request, redirect, url_for
import random
import string

app = Flask(__name__)

def generate_password(length):
    characters = string.ascii_letters + string.digits + string.punctuation
    password = ''.join(random.choice(characters) for _ in range(length))
    return password

@app.route('/', methods=['GET', 'POST'])
def home():
    if request.method == 'POST':
        length = request.form.get('length', type=int)
        if length >= 4:
            password = generate_password(length)
            return render_template('result.html', password=password)
        else:
            password = "Password length should be at least 4."
            return render_template('index.html', password=password)
    return render_template('index.html')

if __name__ == "__main__":
    app.run(debug=True)
