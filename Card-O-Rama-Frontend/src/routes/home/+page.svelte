<script>
   import { fetchGet } from "$lib/network-utils";
   import { GlobalReferences } from "$lib/globals";

   let globalReferences = new GlobalReferences();
</script>

<div id="homepage-content">
   <div id="homepage-info">
      <h1 id="homepage-title">Card-O-Rama</h1>
      <p id="homepage-desc">Make flashcards and make connections!</p>
   </div>
   <div id="homepage-img-container">
      <img id="homepage-img" src="/images/book-open-page-variant.svg" alt="open book">
   </div> 
   <div id="study-set-link-container">
      <a id="study-set-link" href="{globalReferences.indexlocation}/home/flashcards">My Flashcard Sets</a>
   </div> 
</div>

<style>
   @import "/static/styles/main.css";

   #homepage-title, #homepage-desc {
      color: var(--primary-bg-color);
   }

   #homepage-title {
      font-size: 42px;
      text-align: center;
   }

   #homepage-desc {
      font-size: 32px;
      text-align: center;
   }

   #homepage-img {
      width: 306px;
      height: 270px;
   }

   #homepage-content {
      display: flex;
      flex-direction: column;
      justify-content: space-between;
      height: 100%;
   }

   #homepage-info {
      flex: 0 1 200px;
      align-self: center;
      display: flex;
      align-items: center;
      flex-direction: column;
      justify-content: center;
   }

   #homepage-img-container {
      flex: 1;
      align-self: center;
      display: flex;
      align-items: center;
   }

   #study-set-link-container {
      flex: 0 1 250px;
      align-self: center;
      display: flex;
      justify-content: center;
      align-items: center;
   }

   #study-set-link {
      padding: 25px 125px;
      font-size: 28px;
      color: white;
      background-color: var(--primary-bg-color);
      text-decoration: none;
   }
   
   @media screen and (min-width: 769px) {
      #homepage-img {
         width: 381px;
         height: 336px;
      }

      #study-set-link {
         padding: 30px 175px;
         font-size: 36px;
      }

      #homepage-title {
         font-size: 48px;
      }

      #homepage-desc {
         font-size: 38px;
      }
   }

</style>