from flask import Flask, render_template, request, redirect, url_for
from datetime import datetime

app = Flask(__name__)

# Sample in-memory task storage
tasks = []

class Task:
    def __init__(self, title, description, deadline, priority):
        self.title = title
        self.description = description
        self.deadline = datetime.strptime(deadline, '%Y-%m-%d %H:%M')
        self.priority = priority
        self.completed = False

@app.route('/')
def index():
    # Sort tasks by priority and deadline
    sorted_tasks = sorted(tasks, key=lambda t: (t.priority, t.deadline))
    return render_template('index.html', tasks=sorted_tasks)

@app.route('/add', methods=['POST'])
def add_task():
    title = request.form['title']
    description = request.form['description']
    deadline = request.form['deadline']
    priority = int(request.form['priority'])  # Ensure priority is an int
    new_task = Task(title, description, deadline, priority)
    tasks.append(new_task)
    return redirect(url_for('index'))

@app.route('/complete/<int:task_id>')
def complete_task(task_id):
    tasks[task_id].completed = True
    return redirect(url_for('index'))

@app.route('/delete/<int:task_id>')
def delete_task(task_id):
    tasks.pop(task_id)
    return redirect(url_for('index'))

@app.route('/filter', methods=['POST'])
def filter_tasks():
    filter_priority = request.form['filter_priority']
    filtered_tasks = [task for task in tasks if str(task.priority) == filter_priority]
    return render_template('index.html', tasks=filtered_tasks)

if __name__ == '__main__':
    app.run(debug=True)
