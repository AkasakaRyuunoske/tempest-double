//shows or hides the menu when button is clicked
function toggleMenu() {
    const menu = document.getElementById("dropdown-menu");
    menu.classList.toggle("show");
}

//closes menu if clicked outside
document.addEventListener('click', function (event) {
   const menu = document.getElementById("dropdown-menu");
   const button = document.querySelector(".menu-button");

   //verifies if click is done outside of menu and button
   if (!menu.contains(event.target) && !button.contains(event.target)) {
       menu.classList.remove("show");
   }
});

// activates element when visible
function handleScrollAnimation() {
    const elements = document.querySelectorAll('.animate-slide-in-left');
    elements.forEach(element => {
        const position = element.getBoundingClientRect();
        if (position.top < window.innerHeight && position.bottom >= 0) {
            element.classList.add('show');
        }
    });
}

window.addEventListener('scroll', handleScrollAnimation);
window.addEventListener('load', handleScrollAnimation);