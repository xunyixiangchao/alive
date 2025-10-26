package com.example.alive.ui.adapter;

/**
 * 任务列表适配器
 * 用于在TaskListFragment中显示所有处理任务的列表
 * 每个任务项显示缩略图、状态、创建时间、收藏标志等信息
 *
 * 使用ListAdapter提高性能，DiffUtil会自动计算差异并只更新变化的项
 * 只有COMPLETED状态的任务可以点击，用于跳转到Fragment5查看详细结果
 *
 * @property onTaskClick 任务被点击时的回调，参数为Task对象（仅COMPLETED任务会触发）
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0007\u0018\u00002\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00030\u0001:\u0002\u0010\u0011B\u0019\u0012\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00060\u0005\u00a2\u0006\u0002\u0010\u0007J\u0018\u0010\b\u001a\u00020\u00062\u0006\u0010\t\u001a\u00020\u00032\u0006\u0010\n\u001a\u00020\u000bH\u0016J\u0018\u0010\f\u001a\u00020\u00032\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u000bH\u0016R\u001a\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00060\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0012"}, d2 = {"Lcom/example/alive/ui/adapter/TaskListAdapter;", "Landroidx/recyclerview/widget/ListAdapter;", "Lcom/example/alive/data/entity/Task;", "Lcom/example/alive/ui/adapter/TaskListAdapter$TaskViewHolder;", "onTaskClick", "Lkotlin/Function1;", "", "(Lkotlin/jvm/functions/Function1;)V", "onBindViewHolder", "holder", "position", "", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "TaskDiffCallback", "TaskViewHolder", "app_debug"})
public final class TaskListAdapter extends androidx.recyclerview.widget.ListAdapter<com.example.alive.data.entity.Task, com.example.alive.ui.adapter.TaskListAdapter.TaskViewHolder> {
    @org.jetbrains.annotations.NotNull()
    private final kotlin.jvm.functions.Function1<com.example.alive.data.entity.Task, kotlin.Unit> onTaskClick = null;
    
    public TaskListAdapter(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.example.alive.data.entity.Task, kotlin.Unit> onTaskClick) {
        super(null);
    }
    
    /**
     * 创建ViewHolder
     * 在列表需要显示新的任务时被调用
     *
     * @param parent 父ViewGroup（RecyclerView）
     * @param viewType 视图类型（本适配器中始终为0）
     * @return 新创建的TaskViewHolder实例
     */
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public com.example.alive.ui.adapter.TaskListAdapter.TaskViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.ViewGroup parent, int viewType) {
        return null;
    }
    
    /**
     * 绑定数据到ViewHolder
     * 在列表需要显示任务数据时被调用
     *
     * @param holder 要绑定数据的ViewHolder
     * @param position 该项在列表中的位置
     */
    @java.lang.Override()
    public void onBindViewHolder(@org.jetbrains.annotations.NotNull()
    com.example.alive.ui.adapter.TaskListAdapter.TaskViewHolder holder, int position) {
    }
    
    /**
     * DiffUtil回调
     * 用于计算列表中两个Task对象之间的差异
     * ListAdapter根据这个回调来决定哪些项需要更新
     */
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0003J\u0018\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00022\u0006\u0010\u0007\u001a\u00020\u0002H\u0016J\u0018\u0010\b\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00022\u0006\u0010\u0007\u001a\u00020\u0002H\u0016\u00a8\u0006\t"}, d2 = {"Lcom/example/alive/ui/adapter/TaskListAdapter$TaskDiffCallback;", "Landroidx/recyclerview/widget/DiffUtil$ItemCallback;", "Lcom/example/alive/data/entity/Task;", "()V", "areContentsTheSame", "", "oldItem", "newItem", "areItemsTheSame", "app_debug"})
    static final class TaskDiffCallback extends androidx.recyclerview.widget.DiffUtil.ItemCallback<com.example.alive.data.entity.Task> {
        
        public TaskDiffCallback() {
            super();
        }
        
        /**
         * 判断两个项是否表示同一个任务
         * 通过比较任务ID来判断
         *
         * @param oldItem 旧的任务
         * @param newItem 新的任务
         * @return 如果两者是同一个任务则返回true
         */
        @java.lang.Override()
        public boolean areItemsTheSame(@org.jetbrains.annotations.NotNull()
        com.example.alive.data.entity.Task oldItem, @org.jetbrains.annotations.NotNull()
        com.example.alive.data.entity.Task newItem) {
            return false;
        }
        
        /**
         * 判断两个任务的内容是否完全相同
         * 如果areItemsTheSame返回true，则调用此方法判断内容是否改变
         *
         * @param oldItem 旧的任务
         * @param newItem 新的任务
         * @return 如果内容完全相同则返回true
         */
        @java.lang.Override()
        public boolean areContentsTheSame(@org.jetbrains.annotations.NotNull()
        com.example.alive.data.entity.Task oldItem, @org.jetbrains.annotations.NotNull()
        com.example.alive.data.entity.Task newItem) {
            return false;
        }
    }
    
    /**
     * 任务项的ViewHolder
     * 负责管理单个任务项的UI组件和交互逻辑
     *
     * @property binding item_task布局的ViewBinding
     * @property onTaskClick 任务被点击时的回调
     */
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0004\b\u0007\u0018\u00002\u00020\u0001B!\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00070\u0005\u00a2\u0006\u0002\u0010\bJ\u000e\u0010\t\u001a\u00020\u00072\u0006\u0010\n\u001a\u00020\u0006R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00070\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000b"}, d2 = {"Lcom/example/alive/ui/adapter/TaskListAdapter$TaskViewHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "binding", "Lcom/example/alive/databinding/ItemTaskBinding;", "onTaskClick", "Lkotlin/Function1;", "Lcom/example/alive/data/entity/Task;", "", "(Lcom/example/alive/databinding/ItemTaskBinding;Lkotlin/jvm/functions/Function1;)V", "bind", "task", "app_debug"})
    public static final class TaskViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        @org.jetbrains.annotations.NotNull()
        private final com.example.alive.databinding.ItemTaskBinding binding = null;
        @org.jetbrains.annotations.NotNull()
        private final kotlin.jvm.functions.Function1<com.example.alive.data.entity.Task, kotlin.Unit> onTaskClick = null;
        
        public TaskViewHolder(@org.jetbrains.annotations.NotNull()
        com.example.alive.databinding.ItemTaskBinding binding, @org.jetbrains.annotations.NotNull()
        kotlin.jvm.functions.Function1<? super com.example.alive.data.entity.Task, kotlin.Unit> onTaskClick) {
            super(null);
        }
        
        /**
         * 将任务数据绑定到UI
         * 设置状态、时间、缩略图、收藏标志，注册点击监听器
         *
         * @param task 要显示的任务对象
         */
        public final void bind(@org.jetbrains.annotations.NotNull()
        com.example.alive.data.entity.Task task) {
        }
    }
}