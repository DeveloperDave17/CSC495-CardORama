const frontendPort = 5173;
const backendPort = 8082;

export class GlobalReferences {
   constructor() {
      this.indexlocation = `http://localhost:${frontendPort}`;
      this.backendBasePath = `http://localhost:${backendPort}`;
   }
}