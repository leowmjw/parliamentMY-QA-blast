Malaysian Parliament Hansard Blaster!!
======================================

Blast up (POW!!) Malaysian Parliamentary Hansard as debated in Parliament;
for further processing, tagging and loading into SinarProject's Parliamentary CMS.

Each Hansard set will be in a single PDF (e.g. DR-18062015.PDF).  Latest available
can always be downloaded from the official [[Malaysian Parliament]](http://www.parlimen.gov.my)
website.

Usage:
- Assumes a UNIX-like environment (Linux/MacOSX/Windows w/BASH)
- At the top level of the source code repo; put all PDFs to be processed into
  "source" folder.
- Once done processing, the results will be in the "results" folder with folders
  corresponding to source filename containing the "blasted" PDFs based on detected "Topic Title"
- For debugging; logs are stored inside the "logs" folder

Ensure source files are present:
```
leow$ ls source/
DR-17062015.PDF	DR-18062015.PDF	DR-24062013.pdf	README.md
```

Run the script:
```
leow$ ./bin/my-parliament-hansard-split
```

Sample run + output:
[![asciicast](https://asciinema.org/a/6rcyjntbh75cyizp7bcr638qe.png)](https://asciinema.org/a/6rcyjntbh75cyizp7bcr638qe)
