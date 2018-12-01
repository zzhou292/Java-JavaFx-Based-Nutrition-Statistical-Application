# p5
Development Log:
@Jason Zhou All Rights Reserved
Collaborators Are Not Allowed To Copy From This Document.
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

Nov 30 -- TO DO LIST
1.Load and Save Function Implementation Needed
2.Load and Save Stages Implementation Needed
3.Awaiting BplusTree Implementation - James Higgins, Yulu Zou, Kejia Fan

------------------------------------------
TO BE UPDATED
