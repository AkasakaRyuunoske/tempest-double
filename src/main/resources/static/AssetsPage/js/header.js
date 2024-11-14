document.addEventListener('DOMContentLoaded', function() {
    const menuButton = document.querySelector('.menu-button');
    const navIcons = document.querySelector('nav');

    menuButton.addEventListener('click', function() {
        navIcons.classList.toggle('show');
    });

    document.addEventListener('click', function(event) {
        if (!header.contains(event.target) && navIcons.classList.contains('show')) {
            navIcons.classList.remove('show');
        }
    });
});