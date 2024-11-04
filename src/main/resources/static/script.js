function toggleMenu() {
    const menu = document.getElementById("dropdown-menu");
    const body = document.body;
    menu.classList.toggle("show");
    body.classList.toggle("menu-open");
}

// Funzione per attivare l'animazione quando l'elemento è visibile
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
window.addEventListener('load', handleScrollAnimation); // Esegui all'avvio della pagina
