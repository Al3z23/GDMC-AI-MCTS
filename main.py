from gdpc import Editor, Block, Transform, geometry

editor = Editor(buffering=True)
 
# Get a block
block = editor.getBlock((31,68,-182))

print(block)


