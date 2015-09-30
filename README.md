Malaysian Parliament Blaster!!
==================================
Tools to blast up (POW!!) Malaysian Parliamentary Documents in order to
facilitate transparency and accountability in government functioning.

**NEW:** Hansard Parser engine available.  Learn more at:
./iTextBlast/src/org/sinarproject/hansardparser/README.md

Current Available Parser Engines:
- Parliamentary Written Questions & Answers (QA)
- Parliamentary Hansard

Malaysian Parliament QA Blaster!!
==================================

Blast up (POW!!) Malaysian Parliamentary Written Questions &amp; Answers
not answered in Parliament; for further processing, tagging and loading
into SinarProject's Parliamentary CMS.

Each Question + Answer set will be in a single PDF.

Usage:
- Assumes a UNIX-like environment (Linux/MacOSX/Windows w/BASH)
- At the top level of the source code repo; put all PDFs to be processed into "source" folder.
  Once done processing, the results will be in the "results" folder with folders corresponding
  to source filename containing the "blasted" PDFs based on Question No.
- For debugging; logs are stored inside the "logs" folder
- At the top level of the repo; execute the script ..

```

  # See the source (by Parliament session day) ..
  leow$ ls ./source/
20150325 Parliamentary Reply.pdf	20150525 Parliamentary Reply.pdf	20150609 Parliamentary Reply.pdf	20150617 Parliamentary Reply.pdf
20150518 Parliamentary Reply.pdf	20150526 Parliamentary Reply.pdf	20150610 Parliamentary Reply.pdf	documents-export-2015-06-24.zip
20150519 Parliamentary Reply.pdf	20150527 Parliamentary Reply.pdf	20150611 Parliamentary Reply.pdf	imokman.pdf
20150520 Parliamentary Reply.pdf	20150528 Parliamentary Reply.pdf	20150615 Parliamentary Reply.pdf
20150521 Parliamentary Reply.pdf	20150608 Parliamentary Reply.pdf	20150616 Parliamentary Reply.pdf

  # Run the script ..
  leow$ ./bin/parliamentmy-split

  # See the results structure (by Parliament session day) ..
  leow$ ls ./results/
20150325 Parliamentary Reply	20150521 Parliamentary Reply	20150528 Parliamentary Reply	20150611 Parliamentary Reply	imokman
20150518 Parliamentary Reply	20150525 Parliamentary Reply	20150608 Parliamentary Reply	20150615 Parliamentary Reply
20150519 Parliamentary Reply	20150526 Parliamentary Reply	20150609 Parliamentary Reply	20150616 Parliamentary Reply
20150520 Parliamentary Reply	20150527 Parliamentary Reply	20150610 Parliamentary Reply	20150617 Parliamentary Reply

  # See individual day Q&A broken down
  leow$ ls ./results/20150325\ Parliamentary\ Reply/
soalan-0-intro.pdf		soalan-20.pdf			soalan-34.pdf			soalan-47_1435139147.pdf	soalan-63.pdf
soalan-10.pdf			soalan-22.pdf			soalan-35.pdf			soalan-51.pdf			soalan-64.pdf
soalan-11.pdf			soalan-23.pdf			soalan-36.pdf			soalan-52.pdf			soalan-65.pdf
soalan-12.pdf			soalan-24.pdf			soalan-37.pdf			soalan-53.pdf			soalan-67.pdf
soalan-13.pdf			soalan-25.pdf			soalan-39.pdf			soalan-54.pdf			soalan-70.pdf
soalan-14.pdf			soalan-26.pdf			soalan-40.pdf			soalan-55.pdf			soalan-71.pdf
soalan-15.pdf			soalan-27.pdf			soalan-42.pdf			soalan-57.pdf			soalan-72.pdf
soalan-16.pdf			soalan-28.pdf			soalan-42_1435139147.pdf	soalan-58.pdf			soalan-73.pdf
soalan-17.pdf			soalan-29.pdf			soalan-44.pdf			soalan-59.pdf			soalan-74.pdf
soalan-18.pdf			soalan-30.pdf			soalan-45.pdf			soalan-60.pdf			soalan-77.pdf
soalan-19.pdf			soalan-32.pdf			soalan-47.pdf			soalan-61.pdf

```

Development Environment:
- Developed with Netbeans; so there is example Netbeans Project incorporated
- Dependencies: iText jar (put reference to it in your Java project configuration)

Feel free to fork ^^, report bugs, suggestions improvements and add your own PRs!
