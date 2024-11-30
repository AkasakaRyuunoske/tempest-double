document.addEventListener("DOMContentLoaded", function () {
    jsPlumb.ready(function () {
        // Initialize jsPlumb
        jsPlumb.setContainer("canvas");

        // Function to create a new node
        function createNode(id, label, x, y) {
            const node = document.createElement("div");
            node.id = id;
            node.className = "node";
            node.innerHTML = `
                <span>${label}</span>
                <div class="connection-handle"></div>
            `;
            node.style.left = `${x}px`;
            node.style.top = `${y}px`;
            canvas.appendChild(node);

            // Make the node draggable
            jsPlumb.draggable(node, {containment: "parent"});

            // Enable text editing on double-click
            enableTextEditing(node);

            // Add connection points
            enableConnectionPoints(node);

            return node;
        }

        // Enable text editing on nodes
        function enableTextEditing(node) {
            node.addEventListener("dblclick", function () {
                const currentText = node.querySelector("span").innerText;
                const input = document.createElement("input");
                input.type = "text";
                input.value = currentText;
                input.style.width = "100%";
                input.style.height = "100%";
                input.style.border = "none";
                input.style.textAlign = "center";
                input.style.fontSize = "14px";

                // Replace the text with an input field
                node.innerHTML = `<div class="connection-handle"></div>`;
                node.querySelector(".connection-handle").after(input);
                input.focus();

                // Restore the text when editing is done
                input.addEventListener("blur", function () {
                    const updatedText = input.value.trim();
                    node.innerHTML = `<span>${updatedText || currentText}</span><div class="connection-handle"></div>`;
                });

                // Handle Enter key
                input.addEventListener("keydown", function (e) {
                    if (e.key === "Enter") {
                        input.blur();
                    }
                });
            });
        }

        // Add connection points to the node
        function enableConnectionPoints(node) {
            const positions = ["Top", "Bottom", "Left", "Right"];

            positions.forEach((position) => {
                const connectionHandle = document.createElement("div");
                connectionHandle.className = "connection-handle";
                connectionHandle.dataset.position = position.toLowerCase();
                node.appendChild(connectionHandle);

                // Style each connection handle dynamically
                connectionHandle.style.position = "absolute";
                connectionHandle.style.width = "10px";
                connectionHandle.style.height = "10px";
                connectionHandle.style.backgroundColor = "blue";
                connectionHandle.style.borderRadius = "50%";
                connectionHandle.style.cursor = "pointer";

                switch (position) {
                    case "Top":
                        connectionHandle.style.top = "-5px";
                        connectionHandle.style.left = "50%";
                        connectionHandle.style.transform = "translateX(-50%)";
                        break;
                    case "Bottom":
                        connectionHandle.style.top = "35px";
                        connectionHandle.style.left = "50%";
                        connectionHandle.style.transform = "translateX(-50%)";
                        break;
                    case "Left":
                        connectionHandle.style.left = "-5px";
                        connectionHandle.style.top = "50%";
                        connectionHandle.style.transform = "translateY(-50%)";
                        break;
                    case "Right":
                        connectionHandle.style.right = "-5px";
                        connectionHandle.style.top = "50%";
                        connectionHandle.style.transform = "translateY(-50%)";
                        break;
                }

                // Make the connection handle a source for connections
                jsPlumb.makeSource(connectionHandle, {
                    parent: node,
                    anchor: position,
                    connector: ["Straight"],
                    connectorStyle: {stroke: "blue", strokeWidth: 2},
                    endpoint: ["Dot", {radius: 5}],
                    endpointStyle: {fill: "blue"},
                });
            });

            // Make the node a target for connections
            jsPlumb.makeTarget(node, {
                anchor: "Continuous",
                endpoint: ["Dot", {radius: 5}],
                endpointStyle: {fill: "red"},
            });
        }


        // Prevent duplicate connections
        jsPlumb.bind("beforeDrop", function (info) {
            const existingConnections = jsPlumb.getConnections({
                source: info.sourceId,
                target: info.targetId,
            });

            if (existingConnections.length > 0) {
                console.warn("Duplicate connection detected!");
                return false;
            }
            return true;
        });

        // Apply styles for connection handles
        const style = document.createElement("style");
        style.textContent = `
            .connection-handle {
                width: 10px;
                height: 10px;
                background-color: blue;
                border-radius: 50%;
                position: absolute;
                right: -5px;
                top: 50%;
                transform: translateY(-50%);
                cursor: pointer;
            }
        `;
        document.head.appendChild(style);

        // Get canvas and add initial nodes
        const canvas = document.getElementById("canvas");

        const node1 = createNode("node1", "Wind Turbine", 50, 50);
        const node2 = createNode("node2", "Air Condition", 200, 50);
        const node3 = createNode("node3", "Generic Consumer", 350, 50);

        // Connect initial nodes
        jsPlumb.connect({
            source: "node1",
            target: "node2",
            anchors: ["Right", "Left"],
            connector: "Straight",
            paintStyle: {stroke: "blue", strokeWidth: 2},
            endpoint: ["Dot", {radius: 5}],
            endpointStyle: {fill: "blue"},
        });

        jsPlumb.connect({
            source: "node2",
            target: "node3",
            anchors: ["Right", "Left"],
            connector: "Straight",
            paintStyle: {stroke: "red", strokeWidth: 2},
            endpoint: ["Dot", {radius: 5}],
            endpointStyle: {fill: "red"},
        });

        // Add new node functionality
        document.getElementById("add").addEventListener("click", function () {
            const newId = `node${Date.now()}`;
            const newNode = createNode(newId, "New Node", 300, 300);
            jsPlumb.repaintEverything();
        });
    });
});


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