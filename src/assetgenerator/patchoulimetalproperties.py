root = "../main/resources/data/factoryautomation/patchouli_books/book/en_us/entries/references/"
introstring = """{
  "name": "Properties of Metals",
  "icon": "minecraft:iron_ingot",
  "category": "factoryautomation:references",
  "sortnum": 0,
  "pages": [
    {
      "type": "text",
      "text": "A list of the properties of metals, sorted by property.$(br)$(li)$(l:references/0metals#meltingpoints)Melting Points$(li)$(l:references/0metals#densities)Densities$(li)$(l:references/0metals#mhcs)Mass Heat Capacities"
    },
    {
      "type": "empty"
    }"""
startstring = """,
    {
      "type": "factoryautomation:dictionary","""
joinstring = """
    }"""
endstring = """
    }"""
finalstring = """
  ]
}"""

metalinfos = """	IRON(0, "iron", 1538, 0xFFEAEEF2, 447, 7870),
	GOLD(1, "gold", 10000, 0xFFFAF437, 129, 19300),
	COPPER(2, "copper", 1084, 0xFFFF973D, 385, 8933),
	TIN(3, "tin", 232, 0xFFF7E8E8, 227, 7310),
	BRONZE(4, "bronze", 950, 0xFFFFB201, 355, 8780),
	STEEL(5, "steel", 10000, 0xFF000000, 434, 7854),
	PIG_IRON(6, "pig_iron", 10000, 0xFF000000, 415, 7850),
	MAGMATIC_BRASS(7, "magmatic_brass", 10000, 0xFF000000, 0, 0),
	SILVER(8, "silver", 10000, 0xFF000000, 235, 10500),
	LEAD(9, "lead", 10000, 0xFF000000, 129, 11340),
	NICKEL(10, "nickel", 10000, 0xFF000000, 0, 0),
	ALUMINUM(11, "aluminum", 10000, 0xFF000000, 0, 0),
	ALUMINUM_BRONZE(12, "aluminum_bronze", 10000, 0xFF000000, 0, 0),""".replace("\n", "").replace("\t", "").split("),")
metalinfos.pop()
metals = []
infos = ["meltingpoints", "densities","mhcs"]
infomap = {"meltingpoints":1, "densities":4,"mhcs":3}
fancystringmap = {"meltingpoints": "Melting Points", "densities": "Densities", "mhcs": "Mass Heat Capacities"}
formatmap = {"meltingpoints":"<> \\u00B0C", "densities":"<> kg/m\\u00B3","mhcs":"<> J/(kg*K)"}
for metalin in metalinfos:
  ms = metalin.split(", ")
  ms.pop(0)
  ms[0] = ms[0].replace('"', "")

  metals.append(ms)

ingots = ["minecraft:iron_ingot", "minecraft:gold_ingot", "minecraft:copper_ingot"]
for i in range(3, len(metals)):
  ingots.append("factoryautomation:ingot_" + metals[i][0])

file = open(root + "0metals.json", 'w')
file.write(introstring)
for info in infos:
  file.write(startstring)
  file.write(f'\n      "title": "{fancystringmap.get(info)}"')
  file.write(f',\n      "anchor": "{info}"')
  for i in range(len(metals)):
    file.write(f',\n      "i{i % 8 + 1}": "{ingots[i]}"')
    number = float(metals[i][infomap.get(info)])
    fnumber = f"{number:,.0f}".replace(",", ",")
    file.write(f',\n      "t{i % 8 + 1}": "{formatmap.get(info).replace("<>", fnumber)}"')
    if i == 7:
      file.write(joinstring)
      file.write(startstring)
      file.write(f'\n      "title": "{"cont."}"')
  file.write(endstring)
file.write(finalstring)
file.close()