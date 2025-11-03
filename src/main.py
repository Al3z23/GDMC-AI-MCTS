from gdpc import Editor, Block, Transform, geometry
import subprocess
import time

editor = Editor(buffering=True)

# Devuelve una matriz 3D con los bloques por coordenadas matrix[x][y][z]
def obtain_terrain_matrix(size_x, size_y, size_z, pos_x, pos_y, pos_z):
    print("Obteniendo datos del terreno...")
    inicio = time.time()
    matrix = []
    cont_x = 0
    cont_y = 0
    for y in range(pos_y,pos_y+size_y):
        matrix.append([]) # [ [creo esto], [o esto], ... ]
        for x in range(pos_x,pos_x+size_x):
            matrix[cont_x].append([]) # [ [ [ahora esto] , [o esto] ] , ... ]
            for z in range(pos_z,pos_z+size_z):
                block = editor.getBlock((x,y,z))
                if block == Block('minecraft:air'):
                    matrix[cont_x][cont_y].append(1)
                else:
                    matrix[cont_x][cont_y].append(0)
            cont_y += 1
        cont_x += 1
        cont_y = 0
    fin = time.time()
    tiempo_ejec = fin-inicio
    tiempo_ejec = tiempo_ejec*1000
    tiempo_ejec = round(tiempo_ejec)
    print("Matriz construida exitosamente en ", end= "" )
    print(tiempo_ejec, end= "")
    print("ms.")
    return matrix

#print(obtain_terrain_matrix(20,20,20,31,67,-183))
with open("matrix.txt","w") as f:
    #text = str(obtain_terrain_matrix(2,2,2,31,67,-183)) # prueba
    text = str(obtain_terrain_matrix(30,5,20,17,6,-224))
    f.write(text)
    
#with open("matrix.txt","r") as f:
#    print(f.read())


subprocess.run(["javac","-cp", "jade.jar;gson-2.10.1.jar", ".\src\AgenteProcesamiento.java"], check=True , cwd="C:\\Users\\aleja\\Documents\\GitHub\\GDMC-AI-MCTS")
subprocess.run(["javac","-cp", "jade.jar", ".\src\AgenteRelacion.java"], check=True , cwd="C:\\Users\\aleja\\Documents\\GitHub\\GDMC-AI-MCTS")
subprocess.run(["javac","-cp", "jade.jar", ".\src\AgenteConectividad.java"], check=True , cwd="C:\\Users\\aleja\\Documents\\GitHub\\GDMC-AI-MCTS")
#subprocess.run(["java","-cp","jade.jar","jade.Boot","-gui"])
subprocess.run(["java","-cp","jade.jar;gson-2.10.1.jar;.","jade.Boot","-gui","agenteProc:src.AgenteProcesamiento;agenteRel:src.AgenteRelacion;agenteConec:src.AgenteConectividad"])
#subprocess.run(["java","Prueba"], cwd="C:\\Users\\aleja\\Documents\\GitHub\\GDMC-AI-MCTS")




