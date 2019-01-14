# p5
Development Log:
@Jason Zhou All Rights Reserved
------------------------------------------

Nov 27 - Zhikang Meng
1.Milestone #2 JAVAFx效果建造完成
1.Milestone #2 JAVAFx Homepage implemented

------------------------------------------

Nov 29 - Zhikang Meng, Jason Z.
1.优化了除了SELECT的已实现按钮. 会弹出对话框让用户确认选择.
2.MealList 实现了CLEAR.会清除所有当前MealList的ITEM.
3.实现了Analyze: 会打印当前MealList的ITEM的Name.
4.优化了APPLYSEL: MealList里已有项目不会被再次添加.
5.添加了UNdoSEL:重置FOODLIST里所有的SELECT按钮回到初始状态.

1.Select Button Optimized, an alert window created for user to confirm selection.
2.MealList Clear Function Implemeted : Able to clear MealList.
3.Analyze Function implemented, names of the items will be printed out to the terminal.
4.ApplySel Optimized, repeated items are not allowed in MealList.
4.UndoSel Implemented, Clear MealList while keeping the same selectedfoodlist datainfo

------------------------------------------

Noc 29 - Jason Z.
1.Analyze Function fully implemented, a new stage created to show result
2.Known bugs fixed

------------------------------------------

Nov 30 - Zhikang Meng
1.优化了代码结构 将Summay窗口变更为一个单独的类
2.FOODLISTBOX更名为FoodList
3.为Summary窗口添加了Item Count 以及Ok Button
4. 为Summary窗口添加了模态 没有点击 OK或者X 来关闭Summary窗口 不能回到HomePage.

1.Code structure optimized, a new class has been created for summary stage
2.FoodListBox renamed to FoodList for easier identification
3.Item Count Function implemented in Analyze Stage
4.New operation rule added, users are not allowed to go back to homepage without properly close analyze page

------------------------------------------

Nov 30 -- Jason Z.
1.Program tested, no bugs found.

------------------------------------------

Nov 30 -- Zhikang Meng
1.删除了FoodList里的Remove功能
2.优化了代码结构, Meal Item 的Remove处理移动到了 MealList中.
3.修复了FoodList.update里的一点微小错误.

1.Milestone #2 commented and submitted.
2.Remove Function in FoodList deleted
3.Structure optimized, Remove function in MealIteam has been removed to MealList
4.Bugs in Foodlist.update fixed

------------------------------------------

Dec 1 -- Zhikang Meng
1. 修改了Main.java and FoodList.java:
2. 现在HomePage的FoodList开始时为空, 需要手动Load.
3. 初步实现了SAVE和Load功能.

1. Main.java and FoodList.java bugs fixed
2. Manual Load function implemented
3. Save and Load Functions implemented

------------------------------------------

Dec 2 -- Zhikang Meng, Jason Zhou
修改了复数文件,请重新覆盖所有文件.
1. 分离出了add nutrient rule stage And set name rule stage
2. 窗口现在添加了专门对Name Rule的显示和移除
3. Set Name Rule菜单按钮以及窗口的功能已经完全实现
4. add nutrient rule stage 只是单纯分离  并未添加任何功能.
5. 菜单栏中增加了复数ApplyRule的选项, 但是都没有实现.
对 add nutrient rule stage的建议:
最好不要单纯让用户完全输入. 建议添加ChoiceBox 让用户从6种营养中选择一个 以及从 大于小于等于中选择一个  然后用户只输入营养值. 这样 对用户输入异常处理会非常简单.

1. Add nutrirnt rule and set name search rule functions and stages implemented.
2. Name Rule display and remove implemented.
3. Set Name Rule buttons, menus, functions fully implemented.
4. Apply Rule goal added

TO DO: add rule, search name functions needed to be implemented

------------------------------------------

Dec 2 -- James Higgins, Kejia Fan, Yulu Zhou
1. Made small changes to the height of the window made by MealSummaryStage. The "ok" button is now visible.
2. Bugs found in BPTree.java. (rangeSearch methods and insert method)
	- rangeSearch: does not give all of the values that are required for it to give, "==" comparative gives the same values as ">=".
	- insert: sets up an AVL tree
	
TODO: Fix bugs in BPTree.java, then implement into program

------------------------------------------

Dec 3 -- Zhikang Meng 
1.完全实现了ADD NUTRIENT窗口的所有功能, 新添加的Nutrient RULE 会被显示在QUERY列表中
2.实现了Query的ClearAll 功能  会清空所有已添加的规则.

1. Add Nutrient Function fully implemented, nutrient rule will be displayed in Query List
2. ClearAll Function in Query implemented, all rules will be cleared if ClearAll Function is called.

------------------------------------------

Dec 4 -- Kejia Fan, James Higgins, Yulu Zhou
1. insert method now creates a correct BPTree
2. rangeSearch now properly works for comparatives "==" and ">="
3. Bugs found in BPTree.java. (rangeSearch((Double), "<="))
	- rangeSearch: does not consistantly give all of the values that are required for it to give for comparative "<=".
	
TODO: Fix bug found with rangeSearch((Double), "<="). Implement BPTree into the main program.

------------------------------------------
Dec 20 -- Kejia Fan, James Higgins, Yulu Zhou, Zhikang Meng, Jason Zhou
1.Bugs fixed
2.The program has been completely tested

ATTENTION: Please click load and select "Foodlist.csv" to load food data
