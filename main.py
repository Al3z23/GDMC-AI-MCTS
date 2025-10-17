from gdpc import Editor, Block, Transform, geometry
from pade.misc.utility import display_message, start_loop
from pade.core.agent import Agent
from pade.acl.aid import AID
from sys import argv
 
editor = Editor(buffering=True)
 
# Get a block
block = editor.getBlock((31,68,-182))

print(block)


