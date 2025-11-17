from gdpc import Editor, Block, Transform, geometry
import subprocess
import time
from gdpc.geometry import placeCuboid

editor = Editor(buffering=True)

# Posicion esquina area a generar
posX = -87
posY = 101
posZ = -8
# Tamaño del area donde generar
tamX = 40
tamY = 12
tamZ = 40

with open("pos.txt","w") as f:
    #text = str(obtain_terrain_matrix(2,2,2,31,67,-183)) # prueba
    text = str([[posX,posY,posZ],[tamX,tamY,tamZ]])
    f.write(text)
print("Inicializando agentes...")

# Ejec agentes java
subprocess.run(["javac","-cp", "jade.jar;gson-2.10.1.jar", ".\src\AgenteProcesamiento.java"], check=True , cwd="C:\\Users\\aleja\\Documents\\GitHub\\GDMC-AI-MCTS")
subprocess.run(["javac","-cp", "jade.jar", ".\src\AgenteRelacion.java"], check=True , cwd="C:\\Users\\aleja\\Documents\\GitHub\\GDMC-AI-MCTS")
subprocess.run(["javac","-cp", "jade.jar", ".\src\AgenteConectividad.java"], check=True , cwd="C:\\Users\\aleja\\Documents\\GitHub\\GDMC-AI-MCTS")
#subprocess.run(["java","-cp","jade.jar","jade.Boot","-gui"])
subprocess.run(["java","-cp","jade.jar;gson-2.10.1.jar;.","jade.Boot","-gui","agenteProc:src.AgenteProcesamiento;agenteRel:src.AgenteRelacion;agenteConec:src.AgenteConectividad"])


