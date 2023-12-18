document.addEventListener('DOMContentLoaded', function () {
    if (showPopup) {
        var popup = document.getElementById('votePopup');
        popup.classList.add('success-popup');
        setTimeout(function () {
            popup.style.display = 'none';
        }, 5000); // Hide popup after 5 seconds
    }
});
