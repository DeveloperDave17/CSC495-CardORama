const frontendPort = 8080;
const backendPort = 8080;

export class GlobalReferences {
   constructor() {
      this.indexlocation = `http://localhost:${frontendPort}`;
      this.backendBasePath = `http://localhost:${backendPort}/api`;
   }
}