<style>
    form {
        border:1px solid #ddd;
        width:400px;
        margin-left: 25px; 
        margin-top:30px;
        display: inline-block;
        background-color: #F3F3F3;
        padding-top: 24px;
        padding-bottom: 24px;
        padding-left: 10px;
        padding-right: 10px;
        font-family: Arial;
        font-size: 13px;
    }
    
    

</style>
<!--        margin-left: 25px; -->
<?php
	header('Content-Type: text/html; charset=utf-8');
	$limit = 10;
	$query = isset($_REQUEST['q']) ? $_REQUEST['q'] : false;
	$results = false;
	$row = 1;
	$data1 =array();
	ini_set('auto_detect_line_endings', TRUE);


	function getLinkfromMap($input) 
	{
    	$csv = array_map('str_getcsv', file('mapNBCNewsDataFile.csv'));
	    foreach($csv as $value) 
	    {
        	 if ($value[0] == $input) 
        	 {
             	return $value[1];
        	}
		}
	}

	if ($query)
	{
    	require_once('Apache/Solr/Service.php');
	    $solr = new Apache_Solr_Service('localhost', 8983, '/solr/LohithSearchEngineV4/');
	
    	if (get_magic_quotes_gpc() == 1) 
    	{
        	$query = stripslashes($query); 
        }
		try
    	{
        	$algorithm = isset($_GET['algo']) ? $_GET['algo'] : false;
	        if ($algorithm == "Default Algorithm") 
	        {
            	$results = $solr->search($query, 0, $limit);
        	}
	        else if($algorithm ==="PageRank Algorithm")
	        {
                $results = $solr->search($query, 0, $limit,$arrayName = array('sort' => 'PageRankFile desc'));
        	}
	    }
    	catch (Exception $e) 
    	{
	        die("<html><head><title>SEARCH EXCEPTION</title><body><pre>{$e->__toString()}</pre></body></html>");
    	}
	}
?> 

<html>
	<head>
    	<title>Lohith's Search Engine</title>
	</head> 
	<body>
	<form accept-charset="utf-8" method="get" align="center">
    	<label for="q">Search: </label>
    	<table>
    			<input id="q" name="q" type="text" value="<?php echo htmlspecialchars($query, ENT_QUOTES, 'utf-8'); ?>"/>
	    		<input type="submit"/>
	    </table>
		<table>
				<input type="radio" name="algo" value ="Default Algorithm" <?php if(!isset($_GET['algo']) || (isset($_GET['algo']) && $_GET['algo'] =="Default Algorithm")) echo 'checked="checked"';?>  id="default"> Default - Lucene
    			<input type="radio" name="algo" value = "PageRank Algorithm" <?php if(!isset($_GET['algo']) || (isset($_GET['algo']) && $_GET['algo'] =="PageRank Algorithm")) echo 'checked="checked"';?>  id="pagerank"> PageRank
		</table>
	</form> 

	<?php
		if ($results) 
		{
			$total = (int) $results->response->numFound;
      		$start = min(1, $total);
      		$end = min($limit, $total); ?>
		    <div>Results <?php echo $start; ?> - <?php echo $end;?> of <?php echo $total; ?>:</div>
		    <ol>
        		<?php
		        	// iterate result documents
        			foreach ($results->response->docs as $doc)
        			{ 
        		?>
            	<?php
					// iterate document fields / values
		            	$id = $doc -> og_url;
					    $indexMap = substr($id, strpos($id,"/")+1);
			            $title = $doc -> title;
        			    $fileName = $doc->resourcename;
            			$MapFileIndex = substr($fileName, strrpos($fileName, '/') + 1);
		        	    $size = ((int)$doc -> stream_size)/ 1000 ;
		        	    $desc = $doc -> og_description;
        	    ?>
            		<table class ="jsonTable">
            		<li>
            			ID: <?php echo $fileName;?></br>
						URL: <a href = "<?php if($id != '') {echo $id;} else {echo getLinkfromMap($MapFileIndex);}?>" target="_blank"><?php if($id != '') {echo $id;} else {echo getLinkfromMap($MapFileIndex);}?></a> </br> 
                		Title : <?php if($title != ''){echo $title;} else{ echo "N/A";} ?> </br>
                		Description : <?php if($desc != '') {echo $desc;} else{ echo "N/A"; }?></br>
                    	<!--Link Name : <?php if($id != '') {echo $id;} else { echo getLinkfromMap($MapFileIndex); }?></br>-->
                    	<!--Size : <?php echo $size; ?> KB</br>-->
						<!--Date Created:<?php if($doc->date == "")echo htmlspecialchars("N/A"); echo htmlspecialchars($doc->date); ?></br>
						Author: <?php if($doc->meta_author == "")echo htmlspecialchars("N/A"); echo htmlspecialchars($doc->meta_author); ?></br>-->
            		</li>
            		</table> 
            	<?php
        			} 
        		?>
    		</ol>
	<?php 
		}
	?>
	</body> 
</html>