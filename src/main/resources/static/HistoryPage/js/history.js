const selectAllCheckbox = document.getElementById("select-all");
const cardCheckboxes = document.querySelectorAll('.card-header input[type="checkbox"]');


function handleScrollAnimation() {
    const elements = document.querySelectorAll('.animate-slide-in-left');
    elements.forEach(element => {
        const position = element.getBoundingClientRect();
        if (position.top < window.innerHeight && position.bottom >= 0) {
            element.classList.add('show');
        }
    });
}

// Select/deselect all sessions
selectAllCheckbox.addEventListener('change', function () {
    const isChecked = selectAllCheckbox.checked;
    cardCheckboxes.forEach(checkbox => {
        checkbox.checked = isChecked;
    });
});

// Synchronization of the "select all" state when single sessions are selected
cardCheckboxes.forEach(checkbox => {
    checkbox.addEventListener("change", () => {
        const allChecked = [...cardCheckboxes].every(checkbox => checkbox.checked);
        selectAllCheckbox.checked = allChecked;
    });
});

window.addEventListener('scroll', handleScrollAnimation);
window.addEventListener('load', handleScrollAnimation);