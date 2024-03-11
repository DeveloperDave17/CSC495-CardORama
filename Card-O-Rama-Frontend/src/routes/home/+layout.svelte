<script>
   import { GlobalReferences } from "$lib/globals";

   let globalReferences = new GlobalReferences();

   let innerWidth = 0;
   let menuDisplay = "none";
   let menuDisplayImage = "/images/menu.svg";

   function toggleHamburgerMenu() {
      if (menuDisplay === "none") {
         menuDisplay = "block";
         menuDisplayImage = "/images/close.svg";
      } else {
         menuDisplay = "none";
         menuDisplayImage = "images/menu.svg";
      }
   }
</script>

<svelte:window bind:innerWidth />

<div id="nav-bar">
   <div id="left-nav-bar">
      <img id="nav-menu" src={menuDisplayImage} alt="selection menu" on:click={toggleHamburgerMenu}/>
      <a class="nav-text-links" href="{globalReferences.indexlocation}/home/flashcards">Edit</a>
      <a class="nav-text-links">Guides</a>
      <a class="nav-text-links">Study</a>
      <a class="nav-text-links" href="{globalReferences.indexlocation}/home">Home</a>
   </div>
   <div id="right-nav-bar">
      <img id="nav-friends" src="/images/account-multiple.svg" alt="friends">
      <img id="nav-plus" src="/images/plus.svg" alt="flashcard sets"/> 
   </div>
</div>
{#if innerWidth < 769}
   <div id="hamburger-selection-menu" style="display: {menuDisplay}">
      <a href="{globalReferences.indexlocation}/home/flashcards">Edit</a>
      <a>Guides</a>
      <a>Study</a>
      <a>Friends</a>
      <a href="{globalReferences.indexlocation}/home">Home</a>
   </div>
{/if}

<slot />

<style>
   @import "/static/styles/main.css";

   :global(html) {
      height: 100%;
   }

   :global(body) {
      height: 100%;
      margin: 0px;
      display: flex;
      flex-direction: column;
   }

   #nav-plus, #nav-menu, #nav-friends {
      width: 64px;
      height: 64px;
   }

   #nav-bar {
      height: 100px;
      background-color: var(--primary-bg-color);
      display: flex;
      justify-content: space-between;
      align-items: center;
   }

   #nav-bar > div {
      margin: 18px;
   }

   .nav-text-links {
      color: var(--primary-fg-color);
      font-size: 32px;
   }

   #nav-friends, .nav-text-links {
      display: none;
   }

   #left-nav-bar, #right-nav-bar {
      display: flex;
   }

   #left-nav-bar > *, #right-nav-bar > * {
      margin-left: 40px;
      margin-right: 40px;
   }

   #hamburger-selection-menu {
      display: flex;
      flex-direction: column;
   }

   #hamburger-selection-menu > a {
      padding: 20px;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 24px;
      background-color: var(--primary-bg-color);
      color: white;
      border-style: solid;
      border-width: 0 5px 5px 5px;
      border-color: black;
   }

   #hamburger-selection-menu a:first-child {
      border-width: 5px;
   }



   @media screen and (min-width: 769px) {
      #nav-menu {
         display: none;
      }

      #nav-friends, .nav-text-links {
         display: block;
      }
   }
</style>