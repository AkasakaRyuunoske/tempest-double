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
    const title = document.getElementById("popup-title");
    const description = document.getElementById("popup-description");

    const zoneData = {
        1: { title: "Sicurezza in tempo reale", description: "Sorveglia ogni angolo della tua casa grazie alla " +
                "possibilità di visionare le telecamere in tempo reale." },
        2: { title: "Pianificazione intelligente", description: "Programma gli orari di accensione/spegnimento dei " +
                "dispositivi. Sincronizza le attività con i ritmi quotidiani della casa." },
        3: { title: "Controllo dell'illuminazione", description: "Regola l'illuminazione in base alle esigenze. " +
                "Ottimizza il consumo energetico grazie all'illuminazione automatizzata basata su orari o rilevamento " +
                "di presenza." },
        4: { title: "Sicurezza degli accessi", description: "Controlla le serrature di porte e finestre da remoto. " +
                "Abilita notifiche in tempo reale per ogni accesso registrato." },
        5: { title: "Gestione della temperatura", description: "Configura il termostato per mantenere una temperatura " +
                "ottimale. Riduci i consumi con programmazioni intelligenti e sensori ambientali." },
        6: { title: "Gestione dei contenuti multimediali", description: "Gestisci i tuoi contenuti multimediali con un " +
                "semplice tocco." },
        7: { title: "Sistema di blocco", description: "Assicura la tua casa con un sistema di blocco elettronico di " +
                "ultima generazione." },
        8: { title: "Sensori ambientali", description: "Monitora qualità dell'aria, umidità e altri parametri. Integra " +
                "i dati per automatizzare i sistemi di ventilazione o purificazione." },
        9: { title: "Monitoraggio elettrodomestici", description: "Controlla lo stato e il consumo degli elettrodomestici. " +
                "Pianifica l'uso per ottimizzare l'efficienza energetica." },
        10: { title: "Controllo dei consumi elettrici", description: "Monitora il consumo dei dispositivi connessi. " +
                "Disattiva le prese quando non in uso per ridurre gli sprechi." },
    };

    // Recupera dati per la zona selezionata
    const data = zoneData[zoneId];

    if (data) {
        title.textContent = data.title;
        description.textContent = data.description;
    } else {
        title.textContent = "Zona non definita";
        description.textContent = "Nessuna informazione disponibile per questa zona.";
    }

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
