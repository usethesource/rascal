for f in `find . -name "*.concept"`
do
ed $f <<EOF
,s/Pittfalls/Pitfalls/
w
EOF
done
