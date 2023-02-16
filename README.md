# Assignment A2: Mesh Generator

  - Author #1 [email@mcmaster.ca]
  - Author #2 [email@mcmaster.ca]
  - Author #3 [email@mcmaster.ca]

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

To run the generator, go to the `generator` directory, and use `java -jar` to run the product. The product takes one single argument (so far), the name of the file where the generated mesh will be stored as binary.

```
mosser@azrael A2 % cd generator 
mosser@azrael generator % java -jar generator.jar sample.mesh
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
| F01 | Create list of vertices |------|-------|-----|--------|
| F02 | Create list of segments |------|-------|-----|--------|
| F03 | Create list of Polygons |------|-------|-----|--------|
| F04 | Create list of centroids |------|-------|-----|--------|
| F05 | Reference neighbouring polygons |------|-------|-----|--------|
| F06 | Assign vertex colour and thickness |------|-------|-----|--------|
| F07 | Assign segment colour and thicness |------|-------|-----|--------|
| F08 | Assign centroid colour and thickness |------|-------|-----|--------|
| F09 | Add centroid and segment data to MeshDump |------|-------|-----|--------|
| F10 |       ---        |------|-------|-----|--------|
| F11 |       ---        |------|-------|-----|--------|
| F12 |       ---        |------|-------|-----|--------|
| F13 |       ---        |------|-------|-----|--------|
