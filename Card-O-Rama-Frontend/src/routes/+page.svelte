<div id="page">
   <div id="nav-bar"></div>
   <div class="info-container">
      <h1>Card-O-Rama</h1>
      <p>
         Card-O-Rama is a flashcard based studying website that empowers connection making through a tf-idf similarity algorithm.
      </p>
      <a href="http://localhost:8080/oauth2/authorization/google"><span>Login With&nbsp;</span><span id="g1">G</span><span id="o1">o</span><span id="o2">o</span><span id="g2">g</span><span id="l1">l</span><span id="e1">e</span></a>
      <img id="homepage-img" src="/images/book-open-page-variant.svg" alt="open book">
   </div>
   <div id="footer">Created By David Hennigan</div>
</div>

<style>
   @import "/static/styles/main.css";

   :global(body) {
      margin: 0;
   }

   #nav-bar {
      height: 100px;
      background-color: var(--primary-bg-color);
      display: flex;
   }

   h1 {
      color: var(--primary-bg-color);
      display: flex;
      justify-content: center;
      font-size: 36px;
      width: 300px;
      margin-bottom: 0;
   }

   p {
      color: var(--primary-bg-color);
      font-size: 24px;
      width: 300px;
      text-align: center;
   }

   .info-container {
      display: flex;
      flex-direction: column;
      align-items: center;
      height: 100%;
   }

   #footer {
      display: flex;
      height: 75px;
      background-color: var(--primary-bg-color);
      justify-content: center;
      align-items: center;
      text-align: center;
      font-size: 24px;
      color: white;
   }

   #page {
      display: flex;
      flex-direction: column;
      height: 100vh;
   }

   #homepage-img {
      width: 260px;
      height: 230px;
   }

   a:link, a:visited {
      color: var(--primary-bg-color);
      border-color: var(--primary-bg-color);
      border-style: solid;
      text-align: center;
      display: flex;
      justify-content: center;
      align-items: center;
      background-color: #F5F5F5;
      width: 260px;
      height: 48px;
      font-size: 24px;
   }

   #g1, #g2 {
      color: #4285F4;
   }

   #o1, #e1 {
      color: #EA4335;
   }

   #o2 {
      color: #FBBC05;
   }

   #l1 {
      color: #34A853;
   }

   @media screen and (min-width: 769px) {

      .info-container {
         justify-content: space-evenly;
      }

      #homepage-img {
         width: 381px;
         height: 336px;
      }

      h1, p {
         width: 600px;
      }

      h1 {
         font-size: 48px;
      }

      p {
         font-size: 32px;
      }

      a {
         font-size: 32px;
         width: 381px;
         height: 96px;
      }
   }
</style>