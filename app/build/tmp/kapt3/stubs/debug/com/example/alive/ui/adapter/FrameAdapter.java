package com.example.alive.ui.adapter;

/**
 * 8帧列表适配器
 * 用于在Fragment3中显示提取出来的8帧关键帧
 * 每个帧可点击，用户点击帧后可以在该帧上进行圈选操作
 *
 * 使用ListAdapter提高性能，DiffUtil会自动计算差异并只更新变化的项
 *
 * @property onFrameClick 帧被点击时的回调，参数为帧索引（0-7）
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0007\u0018\u00002\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00030\u0001:\u0002\u0010\u0011B\u0019\u0012\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00070\u0005\u00a2\u0006\u0002\u0010\bJ\u0018\u0010\t\u001a\u00020\u00072\u0006\u0010\n\u001a\u00020\u00032\u0006\u0010\u000b\u001a\u00020\u0006H\u0016J\u0018\u0010\f\u001a\u00020\u00032\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0006H\u0016R\u001a\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00070\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0012"}, d2 = {"Lcom/example/alive/ui/adapter/FrameAdapter;", "Landroidx/recyclerview/widget/ListAdapter;", "Lcom/example/alive/data/entity/FrameData;", "Lcom/example/alive/ui/adapter/FrameAdapter$FrameViewHolder;", "onFrameClick", "Lkotlin/Function1;", "", "", "(Lkotlin/jvm/functions/Function1;)V", "onBindViewHolder", "holder", "position", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "FrameDiffCallback", "FrameViewHolder", "app_debug"})
public final class FrameAdapter extends androidx.recyclerview.widget.ListAdapter<com.example.alive.data.entity.FrameData, com.example.alive.ui.adapter.FrameAdapter.FrameViewHolder> {
    @org.jetbrains.annotations.NotNull()
    private final kotlin.jvm.functions.Function1<java.lang.Integer, kotlin.Unit> onFrameClick = null;
    
    public FrameAdapter(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.Integer, kotlin.Unit> onFrameClick) {
        super(null);
    }
    
    /**
     * 创建ViewHolder
     * 在列表需要显示新的帧时被调用
     *
     * @param parent 父ViewGroup（RecyclerView）
     * @param viewType 视图类型（本适配器中始终为0）
     * @return 新创建的FrameViewHolder实例
     */
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public com.example.alive.ui.adapter.FrameAdapter.FrameViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.ViewGroup parent, int viewType) {
        return null;
    }
    
    /**
     * 绑定数据到ViewHolder
     * 在列表需要显示帧数据时被调用，会将FrameData绑定到相应的UI组件
     *
     * @param holder 要绑定数据的ViewHolder
     * @param position 该项在列表中的位置
     */
    @java.lang.Override()
    public void onBindViewHolder(@org.jetbrains.annotations.NotNull()
    com.example.alive.ui.adapter.FrameAdapter.FrameViewHolder holder, int position) {
    }
    
    /**
     * DiffUtil回调
     * 用于计算列表中两个FrameData对象之间的差异
     * ListAdapter根据这个回调来决定哪些项需要更新，提高RecyclerView的性能
     */
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0003J\u0018\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00022\u0006\u0010\u0007\u001a\u00020\u0002H\u0016J\u0018\u0010\b\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00022\u0006\u0010\u0007\u001a\u00020\u0002H\u0016\u00a8\u0006\t"}, d2 = {"Lcom/example/alive/ui/adapter/FrameAdapter$FrameDiffCallback;", "Landroidx/recyclerview/widget/DiffUtil$ItemCallback;", "Lcom/example/alive/data/entity/FrameData;", "()V", "areContentsTheSame", "", "oldItem", "newItem", "areItemsTheSame", "app_debug"})
    static final class FrameDiffCallback extends androidx.recyclerview.widget.DiffUtil.ItemCallback<com.example.alive.data.entity.FrameData> {
        
        public FrameDiffCallback() {
            super();
        }
        
        /**
         * 判断两个项是否表示同一个对象
         * 通常比较唯一标识符（如ID或帧索引）
         *
         * @param oldItem 旧的帧数据
         * @param newItem 新的帧数据
         * @return 如果两者是同一个帧则返回true
         */
        @java.lang.Override()
        public boolean areItemsTheSame(@org.jetbrains.annotations.NotNull()
        com.example.alive.data.entity.FrameData oldItem, @org.jetbrains.annotations.NotNull()
        com.example.alive.data.entity.FrameData newItem) {
            return false;
        }
        
        /**
         * 判断两个项的内容是否相同
         * 如果areItemsTheSame返回true，则调用此方法判断内容是否改变
         *
         * @param oldItem 旧的帧数据
         * @param newItem 新的帧数据
         * @return 如果内容完全相同则返回true
         */
        @java.lang.Override()
        public boolean areContentsTheSame(@org.jetbrains.annotations.NotNull()
        com.example.alive.data.entity.FrameData oldItem, @org.jetbrains.annotations.NotNull()
        com.example.alive.data.entity.FrameData newItem) {
            return false;
        }
    }
    
    /**
     * 8帧项的ViewHolder
     * 负责管理单个帧项的UI组件和交互逻辑
     *
     * @property binding item_frame布局的ViewBinding
     * @property onFrameClick 帧被点击时的回调
     */
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B!\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00070\u0005\u00a2\u0006\u0002\u0010\bJ\u0016\u0010\t\u001a\u00020\u00072\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u0006R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00070\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\r"}, d2 = {"Lcom/example/alive/ui/adapter/FrameAdapter$FrameViewHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "binding", "Lcom/example/alive/databinding/ItemFrameBinding;", "onFrameClick", "Lkotlin/Function1;", "", "", "(Lcom/example/alive/databinding/ItemFrameBinding;Lkotlin/jvm/functions/Function1;)V", "bind", "frameData", "Lcom/example/alive/data/entity/FrameData;", "position", "app_debug"})
    public static final class FrameViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        @org.jetbrains.annotations.NotNull()
        private final com.example.alive.databinding.ItemFrameBinding binding = null;
        @org.jetbrains.annotations.NotNull()
        private final kotlin.jvm.functions.Function1<java.lang.Integer, kotlin.Unit> onFrameClick = null;
        
        public FrameViewHolder(@org.jetbrains.annotations.NotNull()
        com.example.alive.databinding.ItemFrameBinding binding, @org.jetbrains.annotations.NotNull()
        kotlin.jvm.functions.Function1<? super java.lang.Integer, kotlin.Unit> onFrameClick) {
            super(null);
        }
        
        /**
         * 将帧数据绑定到UI
         * 加载帧图像并设置标签，注册点击监听器
         *
         * @param frameData 要显示的帧数据对象
         * @param position 该帧在列表中的位置（0-7）
         */
        public final void bind(@org.jetbrains.annotations.NotNull()
        com.example.alive.data.entity.FrameData frameData, int position) {
        }
    }
}