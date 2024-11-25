
const maxCapacities = {
    satisfaction: 100,
    production: 200,
    accumulator: 150,
    washingMachine: 5,
    refrigerator: 2,
    airConditioner: 3,
    solarPanel: 10,
    windTurbine: 15,
    accumulatorBar: 20
};

const consumptionGraph = new CanvasJS.Chart("consumptionGraph", {
    title: { text: "Consumption", fontSize: 25, fontFamily: "Verdana"},
    axisY: { title: "MW", includeZero: false },
    data: [{ type: "line", color: "#64749A", dataPoints: [] }]
});

const productionGraph = new CanvasJS.Chart("productionGraph", {
    title: { text: "Production", fontSize: 25, fontFamily: "Verdana"},
    axisY: { title: "MW", includeZero: false },
    data: [{ type: "line", color: "#64749A", dataPoints: [] }]
});

let consumptionData = [];
let productionData = [];
let xValue = 0;

function updateProgressBar(id, value, maxCapacity) {
    const bar = document.getElementById(id);
    const percentage = (value / maxCapacity) * 100;
    bar.style.width = percentage + "%";
    bar.textContent = value.toFixed(2) + " MW";
}

function updateMWValues(currentId, maxId, currentValue, maxValue) {
    document.getElementById(currentId).textContent = currentValue.toFixed(2);
    document.getElementById(maxId).textContent = maxValue.toFixed(2);
}

function updateDashboard() {

    const satisfactionValue = Math.random() * maxCapacities.satisfaction;
    const productionValue = Math.random() * maxCapacities.production;
    const accumulatorValue = Math.random() * maxCapacities.accumulator;

    const consumptionValue = Math.random() * 100;
    consumptionData.push({ x: xValue, y: consumptionValue });
    productionData.push({ x: xValue, y: productionValue });

    if (consumptionData.length > 20) consumptionData.shift();
    if (productionData.length > 20) productionData.shift();

    consumptionGraph.options.data[0].dataPoints = consumptionData;
    productionGraph.options.data[0].dataPoints = productionData;

    consumptionGraph.render();
    productionGraph.render();

    updateProgressBar("satisfactionBar", satisfactionValue, maxCapacities.satisfaction);
    updateProgressBar("productionBar", productionValue, maxCapacities.production);
    updateProgressBar("accumulatorChargeBar", accumulatorValue, maxCapacities.accumulator);

    updateMWValues("current-satisfaction", "max-satisfaction", satisfactionValue, maxCapacities.satisfaction);
    updateMWValues("current-production", "max-production", productionValue, maxCapacities.production);
    updateMWValues("current-charge", "max-charge", accumulatorValue, maxCapacities.accumulator);

    updateProgressBar("washingMachineBar", Math.random() * maxCapacities.washingMachine, maxCapacities.washingMachine);
    updateProgressBar("refrigeratorBar", Math.random() * maxCapacities.refrigerator, maxCapacities.refrigerator);
    updateProgressBar("airConditionerBar", Math.random() * maxCapacities.airConditioner, maxCapacities.airConditioner);
    updateProgressBar("solarPanelBar", Math.random() * maxCapacities.solarPanel, maxCapacities.solarPanel);
    updateProgressBar("windTurbineBar", Math.random() * maxCapacities.windTurbine, maxCapacities.windTurbine);
    updateProgressBar("accumulatorBar", Math.random() * maxCapacities.accumulatorBar, maxCapacities.accumulatorBar);

    xValue++;
}

setInterval(updateDashboard, 1000);