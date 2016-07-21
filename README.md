![Travis PdfMetaModifier master status](https://travis-ci.org/dzavodnikov/PdfBookmarksModifier.svg?branch=master)


Overview
========
Simple CLI utility for save/update Outlines (bookmarks), Metadata and file Attachments into PDF files.


Usage
=====
Outlines (bookmarks)
-------------------- 
Save bookmarks into the text file:

    $ pmm --pdf Book.pdf --save-outlines Book_bookmarks.txt

Update bookmarks from the text file:

    $ pmm --pdf Book.pdf --update-outlines Book_bookmarks.txt


Metadata
--------
Save metadata into the text file:

    $ pmm --pdf Book.pdf --save-metadata Book_metadata.txt

Update metadata from the text file:

    $ pmm --pdf Book.pdf --update-metadata Book_metadata.txt


Attached (embedded) files:
--------------------------
Save all attached (embedded) files from PDF to specified directory:

    $ pmm --pdf Book.pdf --save-attachments Book_attachments

Remove all attached (embedded) files from PDF file:

    $ pmm --pdf Book.pdf --remove-attachments

Add 2 attached (embedded) files (`Cover.png` and `Source.tar.gz`) to PDF file:

    $ pmm --pdf Book.pdf --add-attachment Cover.png --add-attachment Source.tar.gz


Outlines (bookmarks) file format
================================
Bookmarks text file have following format:

    [<tabs or spaces>]<title>[|<page number>]

For example:

    Title 1|3
    Title 2|5
        Title 2.1|5
        Title 2.2|6
            Title 2.2.1|7
            Title 2.2.2|8
        Title 2.3|10
    Appendix
        A1|11
        A2|12


Metadata file format
====================
Metadata text file have following format:

    <key>|[<value>]

For example:

    CreationDate|D:20160419132404+05'00'
    Creator|PlotSoft PDFill 5.0
    ModDate|D:20160419132404+05'00'
    Producer|GPL Ghostscript 9.19
    Title|


License
=======
Distributed under GNU AGPL 3.0.
