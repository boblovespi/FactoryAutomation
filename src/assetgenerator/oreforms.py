forms = ("chunk", "normal_gravel", "rich_gravel", "normal_fine_gravel", "rich_fine_gravel", "concentrated_fine_gravel",
         "normal_dust", "rich_dust", "concentrated_dust", "purified_dust", "rich_pellet", "concentrated_pellet",
         "purified_pellet")
root = "../main/resources/assets/factoryautomation/models/item/"

ore = "cassiterite"  # change this to the ore you want to generate model files for

for form in forms:
    f = open(root + ore + "_" + form + ".json", 'w')
    s = f"\"factoryautomation:items/ores/{ore}/{form}\""
    f.write("""{
  "parent": "item/generated",
  "textures": {
    "layer0": """ + s + """
  }
}
""")
    f.close()
