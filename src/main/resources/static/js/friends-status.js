document.addEventListener('DOMContentLoaded', function () {
    const authenticatedUsernameInput = document.getElementById('authenticated-username');
    const username = authenticatedUsernameInput.value;
    const eventSource = new EventSource('/notification/subscribe');
    eventSource.addEventListener(username, function(event) {
        const eventData = JSON.parse(event.data);
        if (eventData.type === 'USER_STATUS') {
            updateUserStatus(eventData.body.username, eventData.body.online);
        }
    });

    eventSource.addEventListener('error', function(error) {
        console.error('EventSource encountered an error:', error);
    });
});

function updateUserStatus(user, online) {
    const statusSpan = document.getElementById('status-' + user);
    if (statusSpan) {
        statusSpan.classList.remove('bg-success', 'bg-secondary');
        if (online) {
            statusSpan.classList.add('bg-success');
            statusSpan.textContent = 'Online';
        } else {
            statusSpan.classList.add('bg-secondary');
            statusSpan.textContent = 'Offline';
        }
    }
}