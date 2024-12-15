const img = document.getElementById("smart-home-img");
const popup = document.getElementById("popup");
const popupContent = document.getElementById("popup-content")

// Functions needed to calculate clickable zones on the smart home image
function calculateCircleCenter(points) {
    const sumX = points.reduce((sum, point) => sum + point.x, 0);
    const sumY = points.reduce((sum, point) => sum + point.y, 0);
    return { x: sumX / points.length, y: sumY / points.length };
}

function calculateCircleRadius(center, points) {
    return Math.max(...points.map(point =>
        Math.sqrt(Math.pow(point.x - center.x, 2) + Math.pow(point.y - center.y, 2))
    ));
}

function isClickInCircle(clickX, clickY, center, radius) {
    const distance = Math.sqrt(Math.pow(clickX - center.x, 2) + Math.pow(clickY - center.y, 2));
    return distance <= radius;
}

// Data of the zones of the image
const zonesData = [
    { id: 1, points: [ { x: 10.55, y: 3.95 }, { x: 10.55, y: 17 }, { x: 15.05, y: 10.15 }, { x: 6.35, y: 10.15 } ] },
    { id: 2, points: [ { x: 5.75, y: 33.45 }, { x: 5.75, y: 44.35 }, { x: 9.5, y: 38.7 }, { x: 2.25, y: 38.7 } ] },
    { id: 3, points: [ { x: 31.7, y: 10.6 }, { x: 31.7, y: 22.15 }, { x: 35.45, y: 16.6 }, { x: 27.7, y: 16.6 } ] },
    { id: 4, points: [ { x: 27.3, y: 43.65 }, { x: 27.3, y: 57.7 }, { x: 32, y: 50.9 }, { x: 22.75, y: 50.9 } ] },
    { id: 5, points: [ { x: 48.3, y: 15.25 }, { x: 48.3, y: 25.25 }, { x: 51.75, y: 20.1 }, { x: 45.1, y: 20.1 } ] },
    { id: 6, points: [ { x: 60.1, y: 34.15 }, { x: 60.1, y: 44.15 }, { x: 63.35, y: 39 }, { x: 56.7, y: 39 } ] },
    { id: 7, points: [ { x: 72.5, y: 46.4 }, { x: 72.5, y: 59 }, { x: 76.95, y: 52.7 }, { x: 68.3, y: 52.7 } ] },
    { id: 8, points: [ { x: 78.45, y: 17.85 }, { x: 78.45, y: 29.35 }, { x: 82.4, y: 23.8 }, { x: 74.5, y: 23.8 } ] },
    { id: 9, points: [ { x: 91.3, y: 64.55 }, { x: 91.3, y: 80.1 }, { x: 96.45, y: 72.3 }, { x: 86.1, y: 72.3 } ] },
    { id: 10, points: [ { x: 75.5, y: 87.15 }, { x: 75.5, y: 97.15 }, { x: 78.7, y: 92.35 }, { x: 72, y: 92.35 } ] }
];

// Function that makes the zones responsive
function getResponsiveZones(img) {
    const rect = img.getBoundingClientRect();
    return zonesData.map(zone => {
        const absolutePoints = zone.points.map(point => ({
            x: (point.x / 100) * rect.width,
            y: (point.y / 100) * rect.height
        }));
        const center = calculateCircleCenter(absolutePoints);
        const radius = calculateCircleRadius(center, absolutePoints);
        return { id: zone.id, center, radius };
    });
}

function showPopup(zoneId) {
    const popup = document.querySelector('.popup');
    const popupContent = document.querySelector('.popup-content');
    const title = document.getElementById("popup-title");
    const description = document.getElementById("popup-description");

    const zoneData = {
        1: "hai cliccato sulla zona 1",
        2: "Zona 2",
        3: "Zona 3",
        // Add other zones
    };

    title.textContent = `Esempio ${zoneId}`;
    description.textContent = zoneData[zoneId] || "no info";

    popup.classList.add('show');
}

function hidePopup() {
    popup.classList.remove("show");
}

img.addEventListener("click", function (event) {
    const rect = img.getBoundingClientRect();
    const clickX = event.clientX - rect.left;
    const clickY = event.clientY - rect.top;

    const zones = getResponsiveZones(img);
    let found = false;

    zones.forEach(zone => {
        if (isClickInCircle(clickX, clickY, zone.center, zone.radius)) {
            showPopup(zone.id);
            found = true;
        }
    });

    if (!found) {
        console.log("Clic fuori da tutte le icone.");
    }
});

// Closes popup if click on the background
popup.addEventListener('click', (event) => {
    if (event.target === popup) {
        hidePopup();
    }
});

window.addEventListener('scroll', handleScrollAnimation);
window.addEventListener('load', handleScrollAnimation);

// Activates elements when they become visible
function handleScrollAnimation() {
    document.querySelectorAll('.animate-slide-in-left').forEach(element => {
        const position = element.getBoundingClientRect();
        if (position.top < window.innerHeight && position.bottom >= 0) {
            element.classList.add('show');
        }
    });
}
