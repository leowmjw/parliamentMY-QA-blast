Malaysian Electoral Commission (EC) Proposal Blaster!!
=======================================================

Blast up (POW!!) Malaysian Electoral Commission Redelineation Proposals.  
So far, only have Sarawak PDF as example.  The output CSV will be used in 
subsequent step in the process of updating the Shapefiles based on the 2013
Election data.
 
Example document (hopefully!!) can be downloaded from the offical 
[Malaysian Electoral Commission](http://www.spr.gov.my) when the time comes.
This semi-automatic step will hopefully aid to scale out the fully voluntary
members of [Tindak Malaysia](http://www.tindak.org/) to prepare for objection within 30 days.

See example of output from EC Proposal vs TM's Fair Allocation:
[here](http://tindakmalaysia.maps.arcgis.com/apps/Viewer/index.html?appid=5d8c5636c48e4b6493738ac4e8a5bbde)

Usage:
- Assumes a UNIX-like environment (Linux/MacOSX/Windows w/BASH)
- At the top level of the source code repo; put all PDFs to be processed into
  "source" folder.  This will be the complete Proposals offered by EC for all
  Malaysian states.  There should be one PDF per state.
- The code will automatically try to recognize the information and extract; but
  if facing issues; try the debugging steps below ..
- Once done processing, the results will be in the "results" folder with a final 
CSV file bearing the same name as the source file
e.g Source => Sarawak_Proposal.pdf; Result => Sarawak_Proposal.csv
- For debugging; logs are stored inside the "logs" folder

Ensure source files are present:
```
leow$ ls source/
README.md		Sarawak_Proposal.pdf
```

Run the script:
```
leow$ ./bin/my-ec-proposal-extract

PROCESSING Sarawak_Proposal in ./
Inside the function processECFile with meta of json
PROCESSING Sarawak_Proposal in ./
Found FIRST SCHEDULE!!false
Mark page 7 as a NEW Block
Found SECOND SCHEDULE!!
Mark page 8 as a NEW Block
Found THIRD SCHEDULE!!
Mark page 77 as a NEW Block
==============================
 PARSING ERRORS --> 132 errors!!!
DUN: 42 DM: 90 Fixed:90
==============================
========================
  @@@@ Data!!! @@@@     
========================
Final DM count: 887
```

To debug, run the script without outputing CSV:
```
leow$ ./bin/my-ec-proposal-extract json

...
========================
  @@@@ Data!!! @@@@     
========================
Final DM count: 887
KEY:192/01/01 ==> 192,MAS GADING,01,Opar,01,Sebiris,348
KEY:192/01/02 ==> 192,MAS GADING,01,Opar,02,Jangkar,642
KEY:192/01/03 ==> 192,MAS GADING,01,Opar,03,Sebandi,287
KEY:192/01/04 ==> 192,MAS GADING,01,Opar,04,Temaga,486
KEY:192/01/05 ==> 192,MAS GADING,01,Opar,05,Stungkor,397
...

```

Output of the run (CSV file):
```
leow$ cat results/Sarawak_Proposal.csv

FULL_CODE,PAR_CODE,PAR_NAME,DUN_CODE,DUN_NAME,DM_CODE,DM_NAME,POPULATION
192/01/01,192,MAS GADING,01,Opar,01,Sebiris,348
192/01/02,192,MAS GADING,01,Opar,02,Jangkar,642
192/01/03,192,MAS GADING,01,Opar,03,Sebandi,287
192/01/04,192,MAS GADING,01,Opar,04,Temaga,486
192/01/05,192,MAS GADING,01,Opar,05,Stungkor,397
192/01/06,192,MAS GADING,01,Opar,06,Opar,920
192/01/07,192,MAS GADING,01,Opar,07,Serayan,605
192/01/08,192,MAS GADING,01,Opar,08,Rukam,402
...

```


