<template>
  <div class="main-container">
    <div class="left">
      <left />
    </div>
    <div class="main">
      <router-view />
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";
import Left from "./Left.vue";
export default defineComponent({
  components: { Left },
  setup() {},
});
</script>

<style lang="scss" scoped>
.main-container {
  height: 100%;
  width: 100%;
  height: 100vh;
  display: flex;
  .left {
    width: 200px;
  }
  .main {
    width: calc(100% - 200px);
  }
}
</style>
