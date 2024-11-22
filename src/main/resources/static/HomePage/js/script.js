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
    { id: 1, points: [ { x: 10.55, y: 3.95 }, { x: 10.55, y: 16.98 }, { x: 15.03, y: 10.15 }, { x: 6.34, y: 10.15 } ] },
    { id: 2, points: [ { x: 5.73, y: 33.45 }, { x: 5.73, y: 44.37 }, { x: 9.52, y: 38.69 }, { x: 2.24, y: 38.69 } ] },
    { id: 3, points: [ { x: 31.71, y: 10.59 }, { x: 31.71, y: 22.16 }, { x: 35.43, y: 16.59 }, { x: 27.71, y: 16.59 } ] },
    { id: 4, points: [ { x: 27.3, y: 43.66 }, { x: 27.3, y: 57.72 }, { x: 32.12, y: 50.88 }, { x: 22.74, y: 50.88 } ] },
    { id: 5, points: [ { x: 48.31, y: 15.27 }, { x: 48.31, y: 25.27 }, { x: 51.76, y: 20.08 }, { x: 45.10, y: 20.08 } ] },
    { id: 6, points: [ { x: 60.10, y: 34.16 }, { x: 60.10, y: 44.17 }, { x: 63.37, y: 38.98 }, { x: 56.70, y: 38.98 } ] },
    { id: 7, points: [ { x: 72.51, y: 46.39 }, { x: 72.51, y: 58.99 }, { x: 76.96, y: 52.69 }, { x: 68.31, y: 52.69 } ] },
    { id: 8, points: [ { x: 78.44, y: 17.86 }, { x: 78.44, y: 29.35 }, { x: 82.39, y: 23.79 }, { x: 74.49, y: 23.79 } ] },
    { id: 9, points: [ { x: 91.28, y: 64.54 }, { x: 91.28, y: 80.11 }, { x: 96.47, y: 72.32 }, { x: 86.10, y: 72.32 } ] },
    { id: 10, points: [ { x: 75.48, y: 87.14 }, { x: 75.48, y: 97.15 }, { x: 78.69, y: 92.33 }, { x: 72.02, y: 92.33 } ] }
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

// Closes popup if clicked on the background
popup.addEventListener('click', (event) => {
    if (event.target === popup) {
        hidePopup();
    }
});

// Activates elements when they become visible
function handleScrollAnimation() {
    document.querySelectorAll('.animate-slide-in-left').forEach(element => {
        const position = element.getBoundingClientRect();
        if (position.top < window.innerHeight && position.bottom >= 0) {
            element.classList.add('show');
        }
    });
}

window.addEventListener('scroll', handleScrollAnimation);
window.addEventListener('load', handleScrollAnimation);
