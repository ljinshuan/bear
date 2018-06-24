grammar Hello; // 定义语法名

r: 'hello' ID; // 匹配关键词hello 和 标识符
ID: [a-z]+; // 小写字母组成标志符
WS: [ \t\r\n]+ -> skip; // 忽略空格换行等