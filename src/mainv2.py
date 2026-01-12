from gdpc import Editor, Block, Transform, geometry
import subprocess
import time
from gdpc.geometry import placeCuboid

editor = Editor(buffering=True)

# Posicion esquina area a generar
# posX = -87
# posY = 101
# posZ = -8
# POS DEF 1
posX = -99
posY = -1
posZ = -57
# POS DEF 2
# posX = -116
# posY = 19
# posZ = -5
# Tamaño del area donde generar
tamX = 40
tamY = 12
tamZ = 40
# Posicion Plantilla Salas
posS1 = [-89,100, -48]
posS2 = [-77,100, -41]
posS3 = [-59,100, -41]
posS4 = [-49,100, -41]
posS5 = [-39,100, -41]
posS6 = [-25,100, -41]
posS7 = [-9,100, -41]
# Dimension Salas
dimS1 = [7,7,3]
dimS2 = [17,7,17]
dimS3 = [10,4,10]
dimS4 = [14,6,18]
dimS5 = [18,6,14]
dimS6 = [20,7,20]
dimS7 = [15,12,15]

def generate_terrain(file):
    
    # rellenar y vaciar

    if(posY < 0):
        b = Block("minecraft:deepslate")
    else:
        b = Block("minecraft:stone")

    placeCuboid(editor,[posX-4, posY-4, posZ-4],[posX+tamX+3, posY+tamY+3, posZ+tamZ+3],b)
    placeCuboid(editor,[posX+4, posY, posZ+4],[posX+tamX-1, posY+tamY-1, posZ+tamZ-2],Block("minecraft:air"))
    
    # placeCuboid(editor,[posX-4, posY-4, posZ-4],[posX+tamX+3, posY+tamY+3, posZ+tamZ+3],Block("minecraft:air"))
    # placeCuboid(editor,[posX-4, posY-1, posZ-4],[posX+tamX+3, posY-1, posZ+tamZ+3],Block("minecraft:stone"))
    
    # salas
    listas = []

    with open(file,"r") as f:
        for linea in f:
            # Eliminamos saltos de línea y separamos por espacios
            numeros = linea.strip().split()
            # Convertimos cada número de cadena a entero
            numeros = [int(n) for n in numeros]
            # Añadimos la lista de números a la lista principal
            listas.append(numeros)
        
    print(listas) # [ X Z numSala ]

    for pos in listas:
        match pos[2]:
            case 1:
                for i in range(0,dimS1[0]):
                    for j in range(0,dimS1[1]):
                        for k in range(0,dimS1[2]):
                            block = editor.getBlock((posS1[0]+i,posS1[1]+j,posS1[2]+k))
                            editor.placeBlock((posX+pos[0]+i,posY+j,posZ+pos[1]+k),block)
                if(pos[1] == 0):
                    for i in range(0,7):
                        for j in range(1,7):
                            for k in range(1,5):
                                editor.placeBlock((posX+pos[0]+i,posY+j,posZ+pos[1]-k),Block("minecraft:air"))
                    placeCuboid(editor,(posX+pos[0]-1,posY,posZ+pos[1]), (posX+pos[0]-1,posY+7,posZ+pos[1]+2),b)
                    placeCuboid(editor,(posX+pos[0]+7,posY,posZ+pos[1]), (posX+pos[0]+7,posY+7,posZ+pos[1]+2),b)
                    placeCuboid(editor,(posX+pos[0]-1,posY+7,posZ+pos[1]), (posX+pos[0]+7,posY+7,posZ+pos[1]+2),b)
                    placeCuboid(editor,(posX+pos[0],posY,posZ+pos[1]+3), (posX+pos[0]+6,posY+6,posZ+pos[1]+3),Block("minecraft:air"))
            case 2:
                for i in range(0,dimS2[0]):
                    for j in range(0,dimS2[1]):
                        for k in range(0,dimS2[2]):
                            block = editor.getBlock((posS2[0]+i,posS2[1]+j,posS2[2]+k))
                            if(block.id != "minecraft:air"):
                                editor.placeBlock((posX+pos[0]+i,posY+j,posZ+pos[1]+k),block)
            case 3:
                for i in range(0,dimS3[0]):
                    for j in range(0,dimS3[1]):
                        for k in range(0,dimS3[2]):
                            block = editor.getBlock((posS3[0]+i,posS3[1]+j,posS3[2]+k))
                            if(block.id != "minecraft:air"):
                                editor.placeBlock((posX+pos[0]+i,posY+j,posZ+pos[1]+k),block)
            case 4:
                for i in range(0,dimS4[0]):
                    for j in range(0,dimS4[1]):
                        for k in range(0,dimS4[2]):
                            block = editor.getBlock((posS4[0]+i,posS4[1]+j,posS4[2]+k))
                            if(block.id != "minecraft:air"):
                                editor.placeBlock((posX+pos[0]+i,posY+j,posZ+pos[1]+k),block)
            case 5:
                for i in range(0,dimS5[0]):
                    for j in range(0,dimS5[1]):
                        for k in range(0,dimS5[2]):
                            block = editor.getBlock((posS5[0]+i,posS5[1]+j,posS5[2]+k))
                            if(block.id != "minecraft:air"):
                                editor.placeBlock((posX+pos[0]+i,posY+j,posZ+pos[1]+k),block)
            case 6:
                for i in range(0,dimS6[0]):
                    for j in range(0,dimS6[1]):
                        for k in range(0,dimS6[2]):
                            block = editor.getBlock((posS6[0]+i,posS6[1]+j,posS6[2]+k))
                            if(block.id != "minecraft:air"):
                                editor.placeBlock((posX+pos[0]+i,posY+j,posZ+pos[1]+k),block)
            case 7:
                for i in range(0,dimS7[0]):
                    for j in range(0,dimS7[1]):
                        for k in range(0,dimS7[2]):
                            block = editor.getBlock((posS7[0]+i,posS7[1]+j,posS7[2]+k))
                            if(block.id != "minecraft:air"):
                                editor.placeBlock((posX+pos[0]+i,posY+j,posZ+pos[1]+k),block)

def generate_paths():
    for i in range(posX+4,posX+tamX):
        zone = False
        cont = False
        num = 0
        for j in range(posZ+2,posZ+tamZ-1):
            block = editor.getBlock((i,posY,j))
            if (block.id == "minecraft:deepslate_tiles" and (not zone)):
                zone = True
                cont = True
                num += 1
            elif(block.id == "minecraft:air" and zone):
                if num < 2:
                    editor.placeBlock((i,posY,j),Block("minecraft:mud_bricks"))
                cont = False
            elif(block.id == "minecraft:deepslate_tiles" and zone and not cont):
                zone = False
    
    for j in range(posZ+3,posZ+tamZ-1):
        zone = False
        cont = False
        num = 0
        for i in range(posX+4,posX+tamX):
            block = editor.getBlock((i,posY,j))
            if (block.id == "minecraft:deepslate_tiles" and (not zone)):
                zone = True
                cont = True
                num += 1
            elif(block.id == "minecraft:air" and zone):
                if num < 2:
                    editor.placeBlock((i,posY,j),Block("minecraft:mud_bricks"))
                cont = False
            elif(block.id == "minecraft:deepslate_tiles" and zone and not cont):
                zone = False

def generate_walls():
    if(posY < 0):
        b = Block("minecraft:deepslate")
    else:
        b = Block("minecraft:stone")
    for i in range(posX,posX+tamX):
        for j in range(posZ,posZ+tamZ):
            block = editor.getBlock((i,posY,j))
            if block.id == "minecraft:air":
                placeCuboid(editor, (i,posY,j),(i,posY+tamY,j),b) 



                     


with open("pos.txt","w") as f:
    #text = str(obtain_terrain_matrix(2,2,2,31,67,-183)) # prueba
    text = str([[posX,posY,posZ],[tamX,tamY,tamZ]])
    f.write(text)
print("Inicializando agentes...")

# Ejec agentes java
subprocess.run(["javac","-cp", "jade.jar;gson-2.10.1.jar", ".\src\AgenteProcesamiento.java"], check=True , cwd="C:\\Users\\aleja\\Documents\\GitHub\\GDMC-AI-MCTS")
subprocess.run(["javac","-cp", "jade.jar", ".\src\AgenteInterfaz.java"], check=True , cwd="C:\\Users\\aleja\\Documents\\GitHub\\GDMC-AI-MCTS")
# subprocess.run(["javac","-cp", "jade.jar", ".\src\AgenteConectividad.java"], check=True , cwd="C:\\Users\\aleja\\Documents\\GitHub\\GDMC-AI-MCTS")
#subprocess.run(["java","-cp","jade.jar","jade.Boot","-gui"])
# subprocess.run(["java","-cp","jade.jar;gson-2.10.1.jar;.","jade.Boot","-gui","agenteProc:src.AgenteProcesamiento;agenteInterf:src.AgenteInterfaz"])
subprocess.run(["java","-cp","jade.jar;gson-2.10.1.jar;.","jade.Boot","-gui","agenteProc:src.AgenteProcesamiento;agenteInterf:src.AgenteInterfaz"])
# subprocess.run(["java","-cp","jade.jar;gson-2.10.1.jar;.","jade.Boot","-gui","agenteProc:src.AgenteProcesamiento;agenteInterf:src.AgenteInterfaz;agenteConec:src.AgenteConectividad"])
inicio = time.time()
generate_terrain("posSalasGen.txt")
generate_paths()
generate_walls()
fin = time.time()
print("Tiempo de generación: ", fin - inicio, " segundos")