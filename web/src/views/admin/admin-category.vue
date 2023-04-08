<template>
    <a-layout>
        <a-layout-content
                :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
        >
            <p><!--添加查询框-->
                <a-form layout="inline" :model="param"><!--调用param-->
                    <a-form-item>
                        <a-input v-model:value="param.name" placeholder="名称">
                        </a-input>
                    </a-form-item>
                    <a-form-item>
                        <a-button type="primary" @click="handleQuery({page: 1, size: pagination.pageSize})">
                            查询
                        </a-button>
                    </a-form-item>
                    <a-form-item>
                        <a-button type="primary" @click="add()">
                            新增
                        </a-button>
                    </a-form-item>
                </a-form>
            </p>

            <a-table
                    :columns="columns"
                    :row-key="record => record.id"
                    :data-source="categorys"
                    :pagination="pagination"
                    :loading="loading"
                    @change="handleTableChange"
            >
                <template #cover="{ text: cover }">
                    <img v-if="cover" :src="cover" alt="avatar"/>
                </template>
                <template v-slot:action="{ text, record }">
                    <!--a-space空格组件-->
                    <a-space size="small">
                        <a-button type="primary" @click="edit(record)">
                                编辑
                        </a-button><!--handleDelete不能起名关键字-->
                        <a-popconfirm
                            title="删除后不可恢复，确认删除"
                            ok-text="是"
                            cancel-text="否"
                            @confirm="handleDelete(record.id)"
                        >
                            <a-button type="danger">
                                删除
                            </a-button>
                        </a-popconfirm>
                    </a-space>
                </template>
            </a-table>
        </a-layout-content>
    </a-layout>
    <a-modal
        title="分类表单"
        v-model:visible="modalVisible"
        :confirm-loading="modalLoading"
        @ok="handleModalOk"
    >
        <!--model绑定category在下面定义-->
        <a-form :model="category" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
            <a-form-item label="名称">
                <a-input v-model:value="category.name"/>
            </a-form-item>
            <a-form-item label="父分类">
                <a-input v-model:value="category.parent"/>
            </a-form-item>
            <a-form-item label="顺序">
                <a-input v-model:value="category.sort"/>
            </a-form-item>
        </a-form>
    </a-modal>
</template>

<script lang="ts">
import {defineComponent, onMounted, ref} from 'vue';
import axios from 'axios';
import {message} from "ant-design-vue";
import {Tool} from "@/util/tool";


export default defineComponent({
    name: 'AdminCategory',
    setup() {
        const param = ref();
        param.value = {}; //定义并初始化param
        const categorys = ref();
        //分页
        const pagination = ref({
            current: 1,
            pageSize: 10,
            total: 0
        });
        const loading = ref(false);

        const columns = [
            {
                title: '名称',
                dataIndex: 'name'
            },
            {
                title: '父分类',
                key: 'parent',
                dataIndex: 'parent',
            },
            {
                title: '顺序',
                dataIndex: 'sort'
            },
            {
                title: 'Action',
                key: 'action',
                slots: {customRender: 'action'}
            }
        ];

        /**
         * 数据查询
         **/
        const handleQuery = (params: any) => {
            loading.value = true;
            //把params中的page、size传到后端，只写params:params传全部
            axios.get("/category/list",{
                params: {
                    page: params.page,
                    size: params.size,
                    name: param.value.name  //前端输入的名字传递到后端从响应式param拿到
                }
            }).then((response) => {
                    loading.value = false;
                    const data = response.data;
                    if(data.success) {
                        categorys.value = data.content.list;
                        // 重置分页按钮
                        pagination.value.current = params.page;
                        pagination.value.total = data.content.total;
                    }else {
                        message.error(data.message) //data.message 返回后端的自定义的异常处理
                    }

            });
        };

        /**
         * 表格点击页码时触发
         */
        const handleTableChange = (pagination: any) => {
            console.log("看看自带的分页参数都有啥：" + pagination);
            handleQuery({
                page: pagination.current,
                size: pagination.pageSize
            });
        };
        /**
         * 表单
         * 定义变量及方法
         * 保存
         */
        const category = ref({});
        const modalVisible = ref(false);
        const modalLoading = ref(false);
        const handleModalOk = () => {
            modalLoading.value = true; //先弹出窗口
            axios.post("/category/save",category.value).then((response) => {
                modalLoading.value = false;//只要后端有返回就去掉
                const data = response.data; // data = commonResp
                if(data.success) {
                    //保存成功拿到结果后关闭窗口重新加载列表
                    modalVisible.value = false;
                    handleQuery({
                        page: pagination.value.current, //查当前页
                        size: pagination.value.pageSize,
                    });
                }else {
                    message.error(data.message);
                }
            });
        }
        /**
         * 新增
         */
        const add = () => {
            modalVisible.value = true;
            category.value = {}
        };
        /**
         * 编辑
         */
        const edit = (record: any) => {
            modalVisible.value = true;
            category.value = Tool.copy(record); //修改表单时是对复制出的新对象进行修改=》不保存会变修改的问题
        };
        /**
         * 删除
         */
        const handleDelete = (id: number) => {
            axios.delete("/category/delete/" + id).then((response) => {
                const data = response.data; // data = commonResp
                if(data.success) {
                    //重新加载列表
                    handleQuery({
                        page: pagination.value.current, //查当前页
                        size: pagination.value.pageSize,
                    });
                }
            });
        }

        /**
         * 周期函数
         */
        //初始查一次，返回到上面handleQuery方法，page,size需要和后端对应
        onMounted(() => {
            handleQuery({
                page: 1,
                size: pagination.value.pageSize
            });
        });

        return {
            //表格的
            param,
            categorys,
            pagination,
            columns,
            loading,
            handleTableChange,
            handleQuery,

            edit,
            add,
            handleDelete,

            //表单类
            category,
            modalVisible,
            modalLoading,
            handleModalOk
        }
    }
});
</script>

<style scoped>
img {
    width: 50px;
    height: 50px;
}
</style>
