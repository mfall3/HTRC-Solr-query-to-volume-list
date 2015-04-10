HTRC-Solr-query-to-volume-list
==================

Reads keyword terms from a given list in a file, gets the ids for volumes containing those terms from HTRC's Solr Proxy AP, then writes the set of volume ids to a file.

Usage: java -jar HTRC-Solr-query-to-volume-list.jar [query] [endpoint] [outputFile]

## arguments:

+ *query* - lucene query string, default: title:freedom

+ *endpoint* - HTRC Solr Proxy API endpoint, default: http://chinkapin.pti.indiana.edu:9994/solr/meta/select/

+ *outputFile* - filename for output list of volume id, default: output.txt