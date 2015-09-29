JSON and JSONlines manipulation tools; and examples
=====================================================
- Sourced from suggestions made here: http://blog.simeonov.com/2014/01/21/manipulating-json-jsonlines-command-line/
- I've placed a minimal package.json for the tools
- Just invoke "npm install"; assuming you have node and npm setup already ..
- For the tool jq; just invoke "brew install jq"; assuming you have brew setup already
- URLs for tools:
	a) https://stedolan.github.io/jq/download/
	b) https://github.com/ddopson/underscore-cli
	c) https://github.com/trentm/json

Put examples of usage above tools for psot-processing below:
- Scenario #1: Break up array in a .json file to become .jsonl (see http://jsonlines.org/)
	Optional: gzip or bzip it to save more space ..
- Scenario #2: Based on a human curated action file (e.g DSL); take action to clean up incorrect
	or duplicate Speaker (e.g. Idris Jusoh merge with Menteri X [Idris Jusoh]) 
- Scenario #3:
- Scenario #4: Enrich the data with calls to PopIt and MapIt; e.g. look up MPs, their Position and Constituency
 
- Example #1:
Source:
```
leow$ cat speakers-Status\ Sekolah\ Kebangsaan\ Danau\ Perdana\ YB\ Puan\ Teresa\ Kok\ Suh\ Sim\ Seputeh.json  | jq '.'
{
  "AZIZAH_BINTI_DATUK_SERI_PANGLIMA_MOHD_DUN": "Azizah binti Datuk Seri Panglima Mohd. Dun]:",
  "BEBERAPA_AHLI": "Beberapa Ahli:",
  "BERIKUT": "berikut:",
...
  "TUAN_SIM_CHEE_KEONG_BUKIT_MERTAJAM": "Tuan Sim Chee Keong [Bukit Mertajam]:",
  "TUAN_SIM_TONG_HIM_KOTA_MELAKA": "Tuan Sim Tong Him [Kota Melaka]:",
  "TUAN_SIM_TZE_TZIN_BAYAN_BARU": "Tuan Sim Tze Tzin [Bayan Baru]:",
  "TUAN_WILSON_UGAK_ANAK_KUMBONG_HULU_RAJANG": "Tuan Wilson Ugak anak Kumbong [Hulu Rajang]:",
  "TUAN_WONG_TIEN_FATT_WONG_NYUK_FOH_SANDAKAN": "Tuan Wong Tien Fatt @ Wong Nyuk Foh [Sandakan]:",
  "TUAN_YANG_DIPERTUA": "Tuan Yang di-Pertua:"
}
```

Transformed:
```
leow$ cat speakers-Status\ Sekolah\ Kebangsaan\ Danau\ Perdana\ YB\ Puan\ Teresa\ Kok\ Suh\ Sim\ Seputeh.json | jq --stream  '{mykey: .[0]|.[], myorig: .[1] }'
{
  "mykey": "AZIZAH_BINTI_DATUK_SERI_PANGLIMA_MOHD_DUN",
  "myorig": "Azizah binti Datuk Seri Panglima Mohd. Dun]:"
}
{
  "mykey": "BEBERAPA_AHLI",
  "myorig": "Beberapa Ahli:"
}
...
{
  "mykey": "TUAN_WILSON_UGAK_ANAK_KUMBONG_HULU_RAJANG",
  "myorig": "Tuan Wilson Ugak anak Kumbong [Hulu Rajang]:"
}
{
  "mykey": "TUAN_WONG_TIEN_FATT_WONG_NYUK_FOH_SANDAKAN",
  "myorig": "Tuan Wong Tien Fatt @ Wong Nyuk Foh [Sandakan]:"
}
{
  "mykey": "TUAN_YANG_DIPERTUA",
  "myorig": "Tuan Yang di-Pertua:"
}
```

- Example #1a: Raw output as formatted JSON
```
leow$ cat speakers-Status\ Sekolah\ Kebangsaan\ Danau\ Perdana\ YB\ Puan\ Teresa\ Kok\ Suh\ Sim\ Seputeh.json  | jq '.'
{
  "AZIZAH_BINTI_DATUK_SERI_PANGLIMA_MOHD_DUN": "Azizah binti Datuk Seri Panglima Mohd. Dun]:",
  "BEBERAPA_AHLI": "Beberapa Ahli:",
...
  "TUAN_WONG_TIEN_FATT_WONG_NYUK_FOH_SANDAKAN": "Tuan Wong Tien Fatt @ Wong Nyuk Foh [Sandakan]:",
  "TUAN_YANG_DIPERTUA": "Tuan Yang di-Pertua:"
}
```

- Example #1b: Using the standard method to create JSONlines
Keys:
```
leow$ cat speakers-Status\ Sekolah\ Kebangsaan\ Danau\ Perdana\ YB\ Puan\ Teresa\ Kok\ Suh\ Sim\ Seputeh.json  | jq 'keys'
[
  "AZIZAH_BINTI_DATUK_SERI_PANGLIMA_MOHD_DUN",
  "BEBERAPA_AHLI",
...
  "TUAN_WONG_TIEN_FATT_WONG_NYUK_FOH_SANDAKAN",
  "TUAN_YANG_DIPERTUA"
]
```
Values:
```
leow$ cat speakers-Status\ Sekolah\ Kebangsaan\ Danau\ Perdana\ YB\ Puan\ Teresa\ Kok\ Suh\ Sim\ Seputeh.json  | jq '.[]'
"Azizah binti Datuk Seri Panglima Mohd. Dun]:"
"Beberapa Ahli:"
...
"Tuan Wong Tien Fatt @ Wong Nyuk Foh [Sandakan]:"
"Tuan Yang di-Pertua:"
```

- Example #2:
- Example #3:

