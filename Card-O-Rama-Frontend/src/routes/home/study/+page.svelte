<script>
   import { GlobalReferences } from "$lib/globals";
   import { onMount } from "svelte";
   import * as d3 from "d3";

   let globals = new GlobalReferences();

   let studyFlashcardSets = [{flashcard: {term:"test", definition: "testing"}, connections: [{term: ""}, {term: ""}, {term: ""}], flashcardColors: ["FFFFFF", "FFFFFF", "FFFFFF", "FFFFFF"]}];
   let selectedStudySet = studyFlashcardSets[0];
   $: currentFlashcardSide = selectedStudySet.flashcard.term;
   let studyPageDisplay = "none";
   let studySelectionPageDisplay = "flex";
   let userStudySets = [];
   let userStudySetSelection = [];
   let sharedStudySets = [];
   let sharedStudySetSelection = [];
   let currentFlashcardIndex = 0;

   let displayGraph = "none";
   let loadingDisplay = "none";
   let graphSVG;

   onMount(async () => {
      // get all of the users flashcardSets
      const responseToGetAll = await fetch(globals.backendBasePath + "/FlashcardSet/getAll", {
         mode: "same-origin",
         method: "get",
         credentials: "include"
      });

      if (responseToGetAll.ok) {
         userStudySets = await responseToGetAll.json();
      }
   });

   function startStudySession() {
      loadingDisplay = "flex";
      fetch(globals.backendBasePath + "/StudySession/create", {
         mode: "same-origin",
         method: "post",
         headers: {
                     "Content-Type" : "application/json",
               },
         credentials: "include",
         body: JSON.stringify(userStudySetSelection)
      }).then((response) => {
         return response.json();
      }).then((data) => {
         studyFlashcardSets = data;
         if (studyFlashcardSets.length > 0) {
            selectedStudySet = studyFlashcardSets[0];
         }
         studySelectionPageDisplay = "none";
         loadingDisplay = "none";
         studyPageDisplay = "flex";
      })
   }

   function exitStudySession() {
      studyPageDisplay = "none";
      studySelectionPageDisplay = "flex";
   }

   function nextFlashcardSet() {
      currentFlashcardIndex++;
      currentFlashcardIndex = currentFlashcardIndex % studyFlashcardSets.length;
      selectedStudySet = studyFlashcardSets[currentFlashcardIndex];
   }

   function previousFlashcardSet() {
      currentFlashcardIndex--;
      if (currentFlashcardIndex < 0) {
         currentFlashcardIndex = studyFlashcardSets.length - 1;
      }
      selectedStudySet = studyFlashcardSets[currentFlashcardIndex];
   }

   function flipFlashcard() {
      if (currentFlashcardSide === selectedStudySet.flashcard.term) {
         currentFlashcardSide = selectedStudySet.flashcard.definition;
      } else {
         currentFlashcardSide = selectedStudySet.flashcard.term;
      }
   }

   function generateGraph() {
      const nodes = [];
      const links = [];
      let listOfAlreadyUsedTerms = [];
      let targetFlashcardTerm = selectedStudySet.flashcard.term;
      listOfAlreadyUsedTerms.push(targetFlashcardTerm);
      nodes.push({id: targetFlashcardTerm, color: selectedStudySet.flashcardColors[0]});
      let connectedCards = selectedStudySet.connections;
      let colorNum = 1;
      let connectCardsToBeUsed = [];
      for (let connection of connectedCards) {
         if (!listOfAlreadyUsedTerms.includes(connection.term)) {
            nodes.push({id: connection.term, color: selectedStudySet.flashcardColors[colorNum]});
            links.push({source: targetFlashcardTerm, target: connection.term, value: 1, strength: 1});
            listOfAlreadyUsedTerms.push(connection.term);
            connectCardsToBeUsed.push(connection);
         }
         colorNum++;
      }

      let connectedStudySets = [];
      for (let studySet of studyFlashcardSets) {
         for (let connection of connectCardsToBeUsed) {
            if (connection.term === studySet.flashcard.term) {
               connectedStudySets.push(studySet);
            }
         }
      }
      for (let connectedStudySet of connectedStudySets) {
         let connections = connectedStudySet.connections;
         let colorNum = 1;
         for (let connectionOfAConnection of connections) {
               if (!listOfAlreadyUsedTerms.includes(connectionOfAConnection.term)) {
                  nodes.push({id: connectionOfAConnection.term, color: connectedStudySet.flashcardColors[colorNum]});
                  listOfAlreadyUsedTerms.push(connectionOfAConnection.term);  
                  links.push({source: connectedStudySet.flashcard.term, target: connectionOfAConnection.term, value: 1, strength: 1});
               }
               colorNum++;
            }
      }

      // clear the previous graph
      graphSVG.innerHTML = "";

      // generate the chart
      const width = 800;
      const height = 560;

      const svg = d3.select("#connect-graph")
         .attr("width", width)
         .attr("height", height)
         .attr("viewBox", [0, 0, width, height])
         .attr("style", "max-width: 100%; height: auto;");

      const nodeElements = svg.append("g")
         .attr("class", "nodes")
         .selectAll("g")
         .data(nodes)
         .enter()
         .append("circle")
         .attr("r", 12)
         .attr("fill", node => "#" + node.color)
         .attr("stroke", "black");

      const textElements = svg.append("g")
         .selectAll("text")
         .data(nodes)
         .enter().append("text")
            .text(node => node.id)
            .attr("font-size", 15)
            .attr("dx", 15)
            .attr("dy", 4);

      const simulation = d3.forceSimulation(nodes)
         .force("charge", d3.forceManyBody())
         .force("center", d3.forceCenter(width / 2, height / 2));

      const linkElements = svg.append('g')
         .selectAll('line')
         .data(links)
         .enter().append('line')
         .attr('stroke-width', 1)
         .attr('stroke', "#999999")
         .attr('stroke-opacity', 0.6);

      function ticked() {
         nodeElements
            .attr("cx", d => d.x)
            .attr("cy", d => d.y);

         textElements
            .attr("x", node => node.x)
            .attr("y", node => node.y);

         linkElements
            .attr("x1", link => link.source.x)
            .attr("y1", link => link.source.y)
            .attr("x2", link => link.target.x)
            .attr("y2", link => link.target.y);
      }

      simulation.on("tick", ticked);
      simulation.force("link", d3.forceLink(links).id(link => link.id).strength(link => link.strength).distance(160));

      function dragstart(event) {
         event.subject.fx = event.x;
         event.subject.fy = event.y;
         console.log("hey");
      }

      function dragmove(event, d) {
         simulation.alphaTarget(0.7).restart();
         event.subject.fx = event.x;
         event.subject.fy = event.y;
      }

      function dragend(event) {
         simulation.alphaTarget(0);
         event.subject.fx = null;
         event.subject.fy = null;
         console.log("ended");
      }

      nodeElements.call(d3.drag().on("start", dragstart).on("drag", dragmove).on("end", dragend));

      displayGraph = "flex";
   }

   function closeGraph() {
      displayGraph = "none";
   }
</script>

<div id="study-selection" style="display: {studySelectionPageDisplay};">
   <div id="user-study-sets">
      <h1>My Study Sets</h1>
      <div class="study-sets">
      {#each userStudySets as userStudySet}
         <div class="selections">
            <input type="checkbox" id="{userStudySet.setID}" name="{userStudySet.setID}" bind:group={userStudySetSelection} value="{userStudySet.setID}">
            <label for="{userStudySet.setID}">{userStudySet.setName}</label>
         </div>
      {/each}
      </div>
   </div>
   <div id="friends-study-sets">
      <h1>Shared Study Sets</h1>
      <div class="study-sets">
      {#each sharedStudySets as sharedStudySet}
         <div class="selections">
            <input type="checkbox" id="{sharedStudySet.setID}" name="{sharedStudySet.setID}" bind:group={sharedStudySetSelection} value="{sharedStudySet.setID}">
            <label for="{sharedStudySet.setID}">{sharedStudySet.setName}</label>
         </div>
      {/each}
      </div>
   </div>
   <button id="start-study-ses-button" on:click={startStudySession}>Start Study Session</button>
</div>
<div id="study-page" style="display: {studyPageDisplay};">
   <button id="exit-ses-button" on:click={exitStudySession}>Exit Study Session</button>
   <div id="flashcard-nav">
      <button id="prev-button" on:click={previousFlashcardSet}>Prev</button>
      <button on:click={nextFlashcardSet}>Next</button>
   </div>
   <div id="selected-flashcard" on:click={flipFlashcard} role="button" style="background-color: #{selectedStudySet.flashcardColors[0]};"><p>{currentFlashcardSide}</p></div>
   <button id="gen-connect-button" on:click={generateGraph}>Generate Connectivity Graph</button>
   <div id="connections">
      <p>Suggested Connections</p>
      {#each selectedStudySet.connections as connection, i}
         <div class="connection" style="background-color: #{selectedStudySet.flashcardColors[i + 1]};"><p>{connection.term}</p></div>
      {/each}
   </div>
</div>
<div id="graph-modal" style="display: {displayGraph};">
   <div id="graph-pop-up">
      <button id="graph-close" on:click={closeGraph}></button>
      <svg id="connect-graph" bind:this={graphSVG}></svg>
   </div>
</div>
<div id="loading-page" style="display: {loadingDisplay};">
   <div id="loading-content">
      <img id="loading-img" src="/images/loading.svg" alt="loading">
   </div>
</div>

<style>
   @import "/static/styles/main.css";

   #connections {
      font-size: 24px;
   }

   .connection {
      width: 245px;
      height: 59px;
      border: 1px solid black;
      font-size: 24px;
      margin-bottom: 24px;
      box-shadow: 0 4px 4px rgba(0, 0, 0, 0.25);
      display: grid;
      align-items: center;
      justify-content: center;
      overflow: auto;
   }

   .connection p {
      padding: 4px;
      margin: 0;
   }

   #selected-flashcard {
      width: 352px;
      height: 188px;
      box-sizing: border-box;
      font-size: 36px;
      border: 1px solid black;
      box-shadow: 0 4px 4px rgba(0, 0, 0, 0.25);
      margin-bottom: 50px;
      margin-top: 12px;
      display: grid;
      align-items: center;
      justify-content: center;
      overflow: auto;
   }

   #selected-flashcard p {
      padding: 8px;
      margin: 0;
   }

   #flashcard-nav button {
      width: 123px;
      height: 57px;
      font-size: 24px;
      color: white;
      background-color: var(--primary-bg-color);
   }

   #exit-ses-button {
      margin-top: 24px;
      font-size: 24px;
      height: 57px;
      background-color: lightgray;
      color: black;
   }

   #start-study-ses-button {
      margin-top: 48px;
      width: 352px;
      height: 57px;
      font-size: 24px;
      color: white;
      background-color: var(--primary-bg-color);
   }

   #prev-button {
      margin-right: 105px;
   }

   #gen-connect-button {
      background-color: var(--primary-bg-color);
      color: white;
      height: 57px;
      font-size: 24px;
      width: 352px;
   }

   #study-page, #study-selection {
      display: flex;
      flex-direction: column;
      align-items: center;
   }

   #flashcard-nav {
      margin-top: 24px;
   }

   #user-study-sets {
      display: grid;
      align-items: center;
      justify-content: center;
      width: 352px;
   }

   h1 {
      text-align: center;
   }

   .study-sets {
      overflow: auto;
      width: 352px;
      height: 240px;
      background-color: #F5F5F5;
      box-sizing: border-box;
      font-size: 24px;
      border: 1px solid black;
      box-shadow: 0 4px 4px rgba(0, 0, 0, 0.25);
   }

   label {
      display: inline; 
   }

   .selections {
      height: 30px;
      text-overflow: ellipsis;
      overflow: hidden;
      white-space: nowrap;
   }

   input[type="checkbox"] {
      transform: scale(1.25);
      accent-color: var(--primary-bg-color);
   }

   .study-sets::-webkit-scrollbar {
      width: 16px;
   }

   .study-sets::-webkit-scrollbar-track {
      box-shadow: inset 0 0 6px rgba(0, 0, 0, 0.3);
   }

   .study-sets::-webkit-scrollbar-thumb {
      background-color: var(--primary-bg-color);
      outline: 1px solid black;
   }

   #graph-modal {
      position: fixed;
      width: 100%;
      height: 100%;
      background-color: rgba(0, 0, 0, 0.6);
      justify-content: center;
      align-items: center;
   }

   #graph-pop-up {
      background-color: white;
      height: 600px;
      width: 800px;
      display: flex;
      flex-direction: column;
      border: 2px solid black;
   }

   #graph-close{
      height: 40px;
      font-size: 24px;
      width: 40px;
      background: url("/images/close-box.svg");
      box-sizing: border-box;
      padding: 0;
      border: none;
      margin: 0;
      align-self: flex-end;
   }

   #loading-page {
      position: fixed;
      width: 100%;
      height: 100%;
      background-color: rgba(0, 0, 0, 0.6);
      justify-content: center;
      align-items: center;
   }

   #loading-content {
      background-color: white;
      box-sizing: border-box;
      padding: 20px;
      width: 200px;
      height: 200px;
   }

   #loading-img {
      animation: spin 1s linear infinite;
   }

   @keyframes spin {
      0% {
         transform: rotate(0deg);
      }
      100% {
         transform: rotate(360deg);
      }
   }
</style>