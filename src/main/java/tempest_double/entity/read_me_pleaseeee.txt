ogni entita deve avere una struttura associata:
    cartella con nome entita -|
                              | ---> cartella nome_entitaRepository: deve contenere interfaccia con i metodi usati SOLO per fare one-line operazione in db.
                              | ---> cartella nome_entitaService: deve contere classe che ha tutte le operazioni neccessarie
                                        per gestione della entita TRA NE GESTIONE DB SOLO VALIDAZIONE
                              | nomeEntita.java: contiene ORM