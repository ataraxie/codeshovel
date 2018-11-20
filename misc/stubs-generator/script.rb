require 'csv'
require 'json'

REPO_NAME = "lucene-solr"
START_COMMIT = "38bf976cd4b9e324c21664bd7ae3d554df803705"
FUNCTION_START_LINE = 666

arr_of_arrs = CSV.read("csv/"+REPO_NAME+".csv")

$expectedResult = {}
$object = {}
$currentStubFileName = ""
$started = false


def doNext
  $object["expectedResult"] = $expectedResult
  $expectedResult = {}
  filePath = "output/" + $currentStubFileName
  if File.file? filePath
    puts "File exists: " + filePath
  end
  File.write(filePath, JSON.pretty_generate($object))
end


arr_of_arrs.each do |row|
  row.each do |col|
    if col && col.include?(".java")
      if $started
        doNext
      else
        $started = true
      end
      pathSplit = col.split(".java/")
      className = pathSplit[0].split("/").pop()
      functionName = pathSplit[1].split("___")[0].gsub(".json", "")
      $currentStubFileName = "Z_" + REPO_NAME + "-" + className + "-" + functionName.split(":")[0] + ".json"
      filePath = (pathSplit[0] + ".java").split("/").drop(1).join("/")
      puts filePath
      $object = {
          repositoryName: REPO_NAME,
          functionName: functionName,
          filePath: filePath,
          functionStartLine: FUNCTION_START_LINE,
          startCommitName: START_COMMIT
      }
    elsif col =~ /^.*:.*/
      split = col.split(":")
      hash = split[0].gsub("\”", "")
      change = split[1].gsub("\",", "").gsub("\"", "").gsub(" ", "")
      $expectedResult[hash] = change
    end
  end
end

doNext