# Assignment A2: Mesh Generator

  - Keira Laskoski [laskoskk@mcmaster.ca]
  - Lily Porter [portel7@mcmaster.ca]
  - Prerna Prabhu [prabhp3@mcmaster.ca]

## How to run the product

_This section needs to be edited to reflect how the user can interact with thefeature released in your project_

### Installation instructions

This product is handled by Maven, as a multi-module project. We assume here that you have cloned the project in a directory named `A2`

To install the different tooling on your computer, simply run:

```
mosser@azrael A2 % mvn install
```

After installation, you'll find an application named `generator.jar` in the `generator` directory, and a file named `visualizer.jar` in the `visualizer` one. 

### Generator

To run the generator, go to the `generator` directory, and use `java -jar` to run the product. The product can take two or three arguments (so far), the name of the file where the generated mesh will be stored as binary, the type of mesh you want (either grid or irregular) and the mode you would like the mesh to be in (either normal or debug). The first argument must be 'sample.mesh', the second must either be 'GR' for a grid or 'IR' for irregular, and the third should be 'debug' if debug mode is desired (nothing for normal mode).

```
mosser@azrael A2 % cd generator

	* 4 options below to generate a mesh:
mosser@azrael generator % java -jar generator.jar sample.mesh GR debug
	* (this will produce a grid in debug mode)
mosser@azrael generator % java -jar generator.jar sample.mesh GR
	* (this will produce a grid in normal mode)
mosser@azrael generator % java -jar generator.jar sample.mesh IR
	* (this will produce an irregular mesh in normal mode)
mosser@azrael generator % java -jar generator.jar sample.mesh IR debug
	* (this will produce an irregular mesh in debug mode) to be created?

mosser@azrael generator % ls -lh sample.mesh
-rw-r--r--  1 mosser  staff    29K 29 Jan 10:52 sample.mesh
mosser@azrael generator % 
```

### Visualizer

To visualize an existing mesh, go the the `visualizer` directory, and use `java -jar` to run the product. The product take two arguments (so far): the file containing the mesh, and the name of the file to store the visualization (as an SVG image).

```
mosser@azrael A2 % cd visualizer 
mosser@azrael visualizer % java -jar visualizer.jar ../generator/sample.mesh sample.svg

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

-- Insert here your definition of done for your features --

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
| F10 |Create SVG canvas| Keira |-------|-----|P|
| F11 |Implement switching between debug and normal mode| Lily |02/21/2023|02/22/2023|P|
| F12 |Render vertices and centroids on canvas|Lily|02/20/2023|02/22/2023|D|
| F13 |Render segments on canvas|Lily|02/21/2023|02/21/2023|D|
| F14 |Write SVG file|Keira|-------|-----|B(F10-F13)|
| |---IRREGULAR MODE---| | | | |
| F15 |Generate 40 random points| Lily |02/22/2023|02/22/2023|D|
| F16 |Compute Voronoi diagram| | | |P|
| F17 |Apply Lloyd relaxation 5 times| | | |P|
| F18 |Crop the mesh to appropriate size| | | |P|
| F19 |Delaunay's triangle| | | |P|
| F20 |Reorder segments| | | |P|
| F21 |Generate user requested number of points & relaxation level| | | |P|
| F22 |Command line arguments for type of mesh| | | |P|
| F23 | not a feature - Adding Dependancies | | | | |
