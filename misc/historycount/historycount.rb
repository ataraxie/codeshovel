
unless ["java","rb","js"].include?(ARGV[0])
  puts "Usage: historycount [java,rb,js] [search_paths_comma_separated]"
  exit
end

file_extension = ARGV[0]
dir = Dir.pwd
puts dir
output_file = "#{dir}/historycount.out"

glob_path = "#{dir}/**/*.#{file_extension}"

unless ARGV[1].nil?
  paths = []
  search_paths = ARGV[1].split(",")
  search_paths.each do |path|
    paths.push("#{dir}/#{path}/**/*.#{file_extension}")
  end
  glob_path = paths
end

puts "Using paths: #{glob_path}"

histories = []
all_files = Dir.glob(glob_path)
num_files = all_files.length

puts "Files found: #{num_files}"

all_files.each_with_index do |file, index|
  file.gsub!("#{dir}/", "")
  puts "(#{index+1} / #{num_files}) - #{file}"
  output = `git log --follow -M60% --pretty=format:"%h" #{file}`
  obj = { :file_path => file, :history_count => output.split("\n").length }
  histories.push(obj)
end

histories.sort_by! {|obj| -obj[:history_count]}

lines = []
histories.each do |obj|
  lines.push("#{obj[:history_count]} - #{obj[:file_path]}")
end

output_content = lines.join("\n")

if num_files > 0
  puts "Writing history file:#{output_file}"
  File.write(output_file, output_content)
end
