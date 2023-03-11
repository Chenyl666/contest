<template>
    <ContestDetail :key="$route.path"/>
</template>

<script>

import ContestDetail from "@/page/contest/component/ContestDetail";
export default {
  name: "ContestDetailPage",
  components: {ContestDetail},
}
</script>

<style scoped>
  .titleTag{
    font-size: 18px;
    font-weight: bold
  }
  .titleTagBorder{
    display: inline-block;
    padding: 2em 2em;
    cursor: pointer;
  }
  .titleTagBorder:hover{
    color: #2c9fe5;
    background-color: #f6f5f7;
  }
  .titleSelected{
    border-bottom: #2c9fe5 3px solid;
  }
  .boldFront{
    margin-top: 2em;
    color: #666;
    line-height: 16px;
    font-weight: 600;
    padding-left: 6px;
    border-left: 4px solid #2c9fe5;
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
  }
  .rightItem{
    margin-left: 0.5em
  }
</style>