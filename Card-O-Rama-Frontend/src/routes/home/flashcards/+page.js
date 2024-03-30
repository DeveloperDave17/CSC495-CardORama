import { GlobalReferences } from "$lib/globals";

//export const prerender = false;

export const load = async ({ fetch }) => {

   // const fetchFlashcardSets = async () => {
   //    let globals = new GlobalReferences();

   //    let flashcardSets = await fetch(globals.backendBasePath + "/FlashcardSet/getAll")
   //          .then(response => {
   //             if (response.status !== 200) {
   //                window.location.href = globals.indexlocation;
   //             }
   //             response.json();
   //          })
   //          .then(data => {
   //             data;
   //          });
   //    return flashcardSets;
   // }

   // const createFirstFlashcardSet = async () => {
   //    await fetch(globals.backendBasePath + "/FlashcardSet/create", {
   //          method: "post",
   //          headers: {
   //                "Content-Type" : "application/json",
   //          },
   //          body: `{
   //             "setName": "Flashcard Set 1",
   //             "privacy": "PRIVATE",
   //             "color": "#FFFFFF",
   //             "priority": "1"
   //          }`
   //          }).then(response => {
   //             responseTest = response;
   //          });
   // }

   // return { 
   //    flashcardSets: fetchFlashcardSets(),
   //  };
}