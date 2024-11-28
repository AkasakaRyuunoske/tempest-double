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
    data: [{ type: "line", color: "#FF8A8A", dataPoints: [] }]
});

const productionGraph = new CanvasJS.Chart("productionGraph", {
    title: { text: "Production", fontSize: 25, fontFamily: "Verdana"},
    axisY: { title: "MW", includeZero: false },
    data: [{ type: "line", color: "#597445", dataPoints: [] }]
});

let consumptionData = [];
let productionData = [];
let xValue = 0;

function updateProgressBar(id, value, maxCapacity, valueDisplayId) {
    const bar = document.getElementById(id);
    const valueDisplay = document.getElementById(valueDisplayId);
    const percentage = (value / maxCapacity) * 100;

    bar.style.width = percentage + "%";

    bar.textContent = "";

    if (valueDisplay) {
        valueDisplay.textContent = value.toFixed(2) + " MW";
    }
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

    updateProgressBar("satisfactionBar", satisfactionValue, maxCapacities.satisfaction, "satisfaction-value");
    updateProgressBar("productionBar", productionValue, maxCapacities.production, "production-value");
    updateProgressBar("accumulatorChargeBar", accumulatorValue, maxCapacities.accumulator, "accumulator-charge-value");

    updateMWValues("current-satisfaction", "max-satisfaction", satisfactionValue, maxCapacities.satisfaction);
    updateMWValues("current-production", "max-production", productionValue, maxCapacities.production);
    updateMWValues("current-charge", "max-charge", accumulatorValue, maxCapacities.accumulator);

    updateProgressBar("washingMachineBar", Math.random() * maxCapacities.washingMachine, maxCapacities.washingMachine, "washing-machine-value");
    updateProgressBar("refrigeratorBar", Math.random() * maxCapacities.refrigerator, maxCapacities.refrigerator, "refrigerator-value");
    updateProgressBar("airConditionerBar", Math.random() * maxCapacities.airConditioner, maxCapacities.airConditioner, "air-conditioner-value");
    updateProgressBar("solarPanelBar", Math.random() * maxCapacities.solarPanel, maxCapacities.solarPanel, "solar-panel-value");
    updateProgressBar("windTurbineBar", Math.random() * maxCapacities.windTurbine, maxCapacities.windTurbine, "wind-turbine-value");
    updateProgressBar("accumulatorBar", Math.random() * maxCapacities.accumulatorBar, maxCapacities.accumulatorBar, "accumulator-value");

    xValue++;
}

setInterval(updateDashboard, 1000);