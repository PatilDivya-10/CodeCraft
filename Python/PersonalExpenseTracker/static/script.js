document.getElementById('add-transaction-btn').addEventListener('click', function() {
    const amountInput = document.getElementById('amount');
    const typeInput = document.getElementById('transaction-type');
    const categoryInput = document.getElementById('category');
    const descriptionInput = document.getElementById('description');
    
    const amount = parseFloat(amountInput.value);
    const type = typeInput.value;
    const category = categoryInput.value;
    const description = descriptionInput.value;

    // Validate amount
    if (isNaN(amount) || amount <= 0) {
        alert("Please enter a valid amount greater than zero.");
        return;
    }

    // Create the transaction data
    const transactionData = {
        amount: amount,
        transaction_type: type,
        category: category,
        description: description
    };

    // Send the transaction data to the server using Fetch API
    fetch('/add', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(transactionData),
    })
    .then(response => {
        if (response.ok) {
            // Update the balance and transaction summary dynamically
            return response.json();
        } else {
            throw new Error('Failed to add transaction.');
        }
    })
    .then(data => {
        // Update balance on the UI
        document.getElementById('balance').innerText = `$${data.new_balance}`;

        // Add the new transaction to the table
        const transactionBody = document.getElementById('transaction-body');
        const newRow = document.createElement('tr');
        newRow.innerHTML = `
            <td>${data.date}</td>
            <td>${type.charAt(0).toUpperCase() + type.slice(1)}</td>
            <td>${category}</td>
            <td>${type === 'credit' ? '' : '-'}$${amount}</td>
            <td>${description}</td>
        `;
        transactionBody.appendChild(newRow);

        // Reset form fields
        amountInput.value = '';
        categoryInput.value = '';
        descriptionInput.value = '';
    })
    .catch(error => {
        alert(error);
    });
});
