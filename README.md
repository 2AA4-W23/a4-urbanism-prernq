# Assignment A4: Urbanism
Authors: Prerna Prabhu [prabhp3@mcmaster.ca]

## Credit Attribution
This project was divided into 3 Sprints, Mesh Generation, Terrain Generation, and Evolution.
The first two sprints were done within a team, with contributions from Lily Porter, Keira Laskoski, and Prerna Prabhu. Each Individual contribution can be found in the backlog of this readme file.

## How to run the product

_This section needs to be edited to reflect how the user can interact with thefeature released in your project_

### Installation instructions

This product is handled by Maven, as a multi-module project. We assume here that you have cloned the project in a directory named `A2`

To install the different tooling on your computer, simply run:

```
mosser@azrael A2 % mvn install
```

After installation, you'll find an application named `generator.jar` in the `generator` directory, a file named 'island.jar' in the 'island' directory, and and a file named `visualizer.jar` in the `visualizer` one. 

### Generator

To run the generator, go to the `generator` directory, and use `java -jar` to run the product. The product can take two - four arguments. The first argument is name of the file where the generated mesh will be stored as binary. The second is the type of mesh you want (enter "GR" for a grid and "IR" for an irregular mesh). If you choose a grid mesh, you do not need enter anymore arguments. If you choose an irregular mesh, you may enter the values (as integers) for the number of points generates and the lloyd relaxation level as arguments 3 and 4.

```
mosser@azrael A2 % cd generator

	* examples for generating a mesh:
mosser@azrael generator % java -jar generator.jar sample.mesh GR 
	* (this will produce a grid mesh)
mosser@azrael generator % java -jar generator.jar sample.mesh IR
	* (this will produce an irregular mesh with the default values of 150 and 5 for points and relaxation)
mosser@azrael generator % java -jar generator.jar sample.mesh IR 500
	* (this will produce an irregular mesh with 500 points and a default value of 5 for relaxation)
mosser@azrael generator % java -jar generator.jar sample.mesh IR 300 8
	* (this will produce an irregular mesh with 300 points and 8 relaxation levels)

mosser@azrael generator % ls -lh sample.mesh
-rw-r--r--  1 mosser  staff    29K 29 Jan 10:52 sample.mesh
mosser@azrael generator % 

```

### Island

To generate an island using an existing mesh, go the the `island` directory, and use `java -jar` to run the product. The product can take 2 arguments: the first is the file containing the mesh and the second is the name of the file to store the island mesh.

```
mosser@azrael A2 % cd island 
mosser@azrael island % java -jar island.jar -i ../generator/sample.mesh -o lagoon.mesh --mode lagoon
mossers@azrael island % ls -lh lagoon.mesh

-rw-r--r--  1 keiralaskoski  staff   167K Mar 14 00:09 lagoon.mesh


```
### PathFinder
To generate cities within the mesh, go to the `island` directory, and use `java -jar` to run the product. The product can take one argument: the number of cities you'd like to produce onto the mesh. It relies on the island generator directory.

```
mosser@azrael A2 % cd island 
mosser@azrael island % java -jar island.jar -i ../generator/sample.mesh -o lagoon.mesh --city 5
mossers@azrael island % ls -lh lagoon.mesh

```



### Visualizer

To visualize an existing mesh, go the the `visualizer` directory, and use `java -jar` to run the product. The product can take 2-3 arguments: the first is the file containing the mesh, the second is the name of the file to store the visualization (as an SVG image), and the third is "-X" which tells the visualizer to output the mesh's debug mode (the third argument is optional, don't enter it if you would like to see the mesh's normal mode).

```
mosser@azrael A2 % cd visualizer 
	*examples for visualizing a mesh:
mosser@azrael visualizer % java -jar visualizer.jar ../generator/sample.mesh sample.svg
	* (this will output a mesh in normal mode)
mosser@azrael visualizer % java -jar visualizer.jar ../generator/sample.mesh sample.svg -X
	* (this will output a mesh in debug mode)
mosser@azrael visualizer % java -jar visualizer.jar ../island/lagoon.mesh sample.svg 
	* (this will output an island mesh)
	
... (lots of debug information printed to stdout) ...

mosser@azrael visualizer % ls -lh sample.svg
-rw-r--r--  1 mosser  staff    56K 29 Jan 10:53 sample.svg
mosser@azrael visualizer %
```
To viualize the SVG file:

  - Open it with a web browser
  - Convert it into something else with tool slike `rsvg-convert`

## How to contribute to the project

When you develop features and enrich the product, remember that you have first to `package` (as in `mvn package`) it so that the `jar` file is re-generated by maven.

## Backlog

### Definition of Done

The program must compile and install successfully in maven with the feature implemented and when run, produce give the desired output in generator and visualizer for multiple test cases.

### Product Backlog
Kanban Board Available in Projects 

| Id  | Feature title  | Start  | End    | Status | MVP? |
| :-: | :-:            | ---    | :-:    | :-:    | :-:  |
| F01 | Intoduce nodes |04-10-23|04-12-23| D      | X    |
| F02 | Intoduce edges |04-10-23|04-12-23| D      | X    |
| F03 | Create graph   |04-10-23|04-12-23| D     | X    |
| F04 | Create shortest path algorithm |04-10-23|04-12-23| D      | X    |
| F05 | Find path between each node |04-10-23|04-12-23| D     |    |
| F06 | Create cities from int value |04-10-23|04-12-23| D      |    |
| F07 | Generate random cities not on water |04-10-23|04-12-23| D      |    |
| F08 | Create capital city |04-10-23|04-12-23| D      |    |
| F09 | Integrate into islandGen |04-10-23|04-12-23| D      |    |
| F10 | Create command line argument for number of cities |04-10-23|04-12-23| D      |    |


# A2: Mesh Generation
Authors:
  - Keira Laskoski [laskoskk@mcmaster.ca]
  - Lily Porter [portel7@mcmaster.ca]
  - Prerna Prabhu [prabhp3@mcmaster.ca]

### Product Backlog

| Id  | Feature title | Who? | Start | End | Status |
| :-: |:-:            |---   | :-:   | :-: | :-:    |
| |---GRID MODE---| | | | | |
| F01 | Create list of vertices | Prerna |02/17/2023|02/20/2023|D|
| F02 | Create list of segments | Prerna |02/17/2023|02/20/2023|D|
| F03 | Create list of Polygons | Prerna |02/19/2023|02/20/2023|D|
| F04 | Create list of centroids | Prerna |02/19/2023|02/21/2023|D|
| F05 | Reference neighbouring polygons | Keira |02/22/22|02/23/22|D|
| F06 | Assign vertex colour and thickness | Lily |02/20/2023|02/20/2023|D|
| F07 | Assign segment colour and thickness | Lily |02/21/2023|02/21/2023|D|
| F08 | Assign centroid colour and thickness | Lily |02/21/2023|02/21/2023|D|
| F09 | Add centroid and segment data to MeshDump | Keira |02/23/22|02/23/22|D|
| F10 |Create SVG canvas| From original code |-------|-----|D|
| F11 |Implement switching between debug and normal mode| Lily |02/21/2023|02/22/2023|D|
| F12 |Render vertices and centroids on canvas|Lily|02/20/2023|02/22/2023|D|
| F13 |Render segments on canvas|Lily|02/21/2023|02/21/2023|D|
| F14 |Write SVG file|From original code|-------|-----|D|
| |---IRREGULAR MODE---| | | | |
| F15 |Generate 40 random points| Lily |02/22/2023|02/22/2023|D|
| F16 |Compute Voronoi diagram| Keira|02/24/2023 | 02/26/2023|D|
| F17 |Apply Lloyd relaxation 5 times|Keira |02/26/2023 | 02/26/2023|D|
| F18 |Crop the mesh to appropriate size| Prerna | 2023/02/25 | 2023/02/26 |D|
| F19 |Delaunay's triangle|Keira |02/27/2023 | 02/27/2023|D|
| F20 |Compute ConvexHull to Reorder segments|Prerna|02/27/23 |02/27/2023 |D|
| F21 |Generate user requested number of points & relaxation level|Lily |02/27/23 |02/27/23 |D|
| F22 |Command line arguments for type of mesh|Lily |02/23/23|02/23/23 |D|
| F23 | not a feature - Adding Dependencies |Keira | 02/24/2023|02/24/2023 | D|


# A3: Terrain Generation
Authors:
  - Keira Laskoski [laskoskk@mcmaster.ca]
  - Lily Porter [portel7@mcmaster.ca]
  - Prerna Prabhu [prabhp3@mcmaster.ca]

### A3 Product Backlog

| Id  | Feature title | Who? | Start | End | Status | MVP? |
| :-: |:-:            |---   | :-:   | :-: | :-:    | :-: |
|F01|Initialize classes & Enums(within them) for the different attributes of each category|Lily|03/09/2023|03/09/2023|D|yes|
|F02|Implement shape interface. Initializing circle shape. Create list of polygons & vertices & segments (like in DotGen)|Keira|03/13/23|03/14/23|D|yes|
|F03|From centre, every polygon located outside of radius 200 is given property biome “ocean” and colour “0,0,204”|Keira|03/14/23|03/15/23|D|yes|
|F04|From centre, every polygon located between radius 200 and 50, given biome “forest” in biome class and colour 0,204,0|Prerna|||D|yes|
|F05|From centre, every polygon located inside radius 50, given biome “lake” in biome class and colour 0,128,255 in colour class|Lily|03/16/2023|03/16/2023|D|yes|
|F06|All polygons with property land and has a neighbouring polygon with biome “Ocean” or biome “lagoon” is changed to colour “255,229,204” and biome “beach”|Prerna|||D|yes|
|F07|Visualize filled polygons with colours in normal mode|Lily|03/17/2023|02/23/23|D|yes|
|F08|Assign property “Elevation” to 0 for each polygon and vertex elevation is the average it’s polygons|Lily|03/16/2023||P|x|
|F09|Assign property “Temperature” to each polygon based on sea level temp and elevation|Lily|03/16/2023|03/16/2023|D|x|
|F10|Assign property “Humidity” to each polygon as set value of 0|Lily|03/18/2023|03/18/2023|D|x|
|F11|Assign every polygon property “Aquifer” as “false”|Lily|03/16/2023|03/16/2023|D|x|
|F12|Assign every segment with property “River” as “false”|Prerna|||D|x|
|F13|Assign every polygon with property “Soil absorption” as “0”|Prerna|||D?|x|
|F14|Initialize random class|Keira|03/22/2023||P|x|
|F15|Randomize elevation levels for polygons (beaches and rest of island) (default/random altimetric profile)|Lily|03/18/2023||P|x|
|F16|Generate lakes from integer value|Prerna|||D|x|
|F17|Generate basic rivers from integer value|Keira|03/30/2023|04/02/2023|D|x|
|F18|Generate lakes based off of rivers with no outlet|Prerna|04/02/2023|04/02/2023|D|x|
|F19|Implement merging rivers and updating discharge level|Keira|04/02/2023|04/02/2023|D|x|
|F20|Generate random locations for aquifers from integer value|Lily|03/18/2023|03/18/2023|D|x|
|F21|Assign humidity values to polygons based on distance from lake, ocean or aquifer|Prerna|| ||D|x|
|F22|Implement soil absorption profile|Keira|||P|x|
|F23|Assign more detailed biome values and colours (colour class) to polygons based on humidity and temperature|Lily|||P|x|
|F24|Add altimetric profile for volcano|Prerna|||P|x|
|F25|Add altimetric profile for flat island|Prerna|||P|x|
|F26|Add altimetric profile for crater|Lily|||P|x|
|F27|Implement oval shaped island|Keira|||P|x|
|F28|Implement random shaped island – use random numbers|Keira|||P|x|
|F29|Implement reproducibility|Keira|||P|x|
|F30|Implement command line argument –shape|Lily|||P|x|
|F31|Implement command line argument –altitude|Prerna|||P|x|
|F32|Implement command line argument –lakes|Lily|||P|x|
|F33|Implement command line argument –rivers|Lily|||P|x|
|F34|Implement command line argument –aquifers|Prerna|||P|x|
|F35|Implement command line argument –soil|Prerna|||P|x|
|F36|Implement command line argument –biomes|Keira|||P|x|
|F37|Implement command line argument –seed|Keira|||P|x|
|F38|Revise editor to output final island mesh (no vertices, only river segments)|Prerna|||P|x|
