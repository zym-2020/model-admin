<template>
  <div>
    <el-form label-width="90px" :model="form">
      <el-form-item label="存储文件名">
        <el-input v-model="form.fileName" :disabled="true" />
      </el-form-item>
      <el-form-item label="学员编号">
        <el-input v-model="form.memberId" :disabled="true" />
      </el-form-item>
      <el-form-item label="作业编号">
        <el-input v-model="form.number" :disabled="true" />
      </el-form-item>
      <el-form-item label="作业状态">
        <el-select v-model="state" placeholder="选择状态">
          <el-option
            v-for="(item, index) in options"
            :key="index"
            :label="item"
            :value="item"
          />
        </el-select>
      </el-form-item>
    </el-form>
    <div class="btn">
      <el-button type="primary" plain size="small" @click="updateClick">确定</el-button>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref } from "vue";
import { updateHomework } from "@/api/request";
export default defineComponent({
  props: {
    formValue: {
      type: Object,
    },
  },
  emits: ["returnUpdateData"],
  setup(props, context) {
    const state = ref((props.formValue as any).state);
    const getType = (number: number) => {
      if (number === 1) {
        return "GeoDetector模型";
      } else if (number === 2) {
        return "GeoSOS模型";
      } else if (number === 3) {
        return "HASM模型";
      } else {
        return "GWmodelS模型";
      }
    };
    const form = {
      fileName: (props.formValue as any).fileName,
      memberId: (props.formValue as any).memberId,
      number: getType((props.formValue as any).number),
    };

    const options = ref([0, 1]);

    const updateClick = async () => {
      const data = await updateHomework(
        (props.formValue as any).id,
        state.value
      );
      if (data != null && (data as any).code === 0) {
        context.emit("returnUpdateData", {
          id: (props.formValue as any).id,
          state: state.value,
        });
      }
    };

    return {
      form,
      options,
      getType,
      updateClick,
      state,
    };
  },
});
</script>

<style lang="scss" scoped>
.btn {
  text-align: center;
}
</style>