<template>
  <div class="editor">
    <!-- <div ref="toolbar" class="toolbar"></div> -->
    <div id="editor" ref="editor" class="text"></div>
  </div>
</template>

<script>
import E from 'wangeditor'

export default {
  data () {
    return {
      flag: false,
      editor: null,
      info_: null
    }
  },

  mounted () {
    this.seteditor()
  },

  methods: {
    write: function (data) {
      this.editor.$txt.html(data)
    },

    fetch: function () {
      return this.editor.$txt.html()
    },

    forbidden: function () {
      this.editor.disable()
    },

    open: function () {
      this.editor.enable()
    },

    clear: function () {
      this.editor.$txt.html('<p><br></p>')
    },

    duibi (dom, data) {
      let _html = document.getElementsByTagName('html')[0]
      if (data !== _html) {
        if (dom !== data) {
          this.duibi(dom, data['parentElement'])
        } else {
          this.flag = true
        }
      } else {
        this.flag = false
      }
    },

    setContentUpload (data, url, file) {
      // let selection = window.getSelection()
      let range = window.getSelection().getRangeAt(0)
      console.log(range)
      let d = document.querySelector('#editor')
      let b = document.createElement('br')
      let p = document.createElement('p')
      p.appendChild(b)
      this.duibi(d, range.commonAncestorContainer)
      if (!this.flag) {
        this.$message.warning('请在文本编辑器内选定您想插入文件的位置')
        return false
      }
      // if (d !== range.commonAncestorContainer && d !== range.commonAncestorContainer.parentElement && d !== range.commonAncestorContainer.parentElement.parentElement && d !== range.commonAncestorContainer.parentElement.parentElement.parentElement) {
      //   this.$message.warning('请在文本编辑器内选定您想插入文件的位置')
      //   return false
      // }
      if (file.raw.type === 'image/png' || file.raw.type === 'image/jpeg' || file.raw.type === 'image/jpg' || file.raw.type === 'image/gif') {
        let i = document.createElement('img')
        let srcString = url + data.key
        i.setAttribute('src', srcString)
        i.style.maxWidth = '100%'
        range.insertNode(i)
        range.setStartAfter(i, 0)

        // this.editor.$txt.append('<img style="max-width:100%" src=' + url + data.key + '></img>')
      }

      if (file.raw.type === 'video/mp4' || file.raw.type === 'video/rmvb' || file.raw.type === 'video/avi' || file.raw.type === 'video/MOV') {
        if (this.getContent()) {
          let v = document.createElement('video')
          let s = document.createElement('source')
          let srcString = url + data.key
          s.setAttribute('src', srcString)
          v.style.width = '90%'
          v.setAttribute('controls', true)
          v.setAttribute('autoplay', true)
          v.setAttribute('src', srcString)
          v.appendChild(s)

          range.insertNode(p)
          range.insertNode(v)
          range.setStartAfter(p, 0)
        } else {
          this.editor.$txt.append('<video style="width: 100%" controls autoplay src=' + url + data.key + '><source src=' + url + data.key + '></video>')
          this.editor.$txt.append('<p><br></p>')
        }
      }

      if (file.raw.type === 'audio/mp3') {
        if (this.getContent()) {
          let v = document.createElement('audio')
          let s = document.createElement('source')
          let srcString = url + data.key
          s.setAttribute('src', srcString)
          v.style.width = '90%'
          v.setAttribute('controls', true)
          v.setAttribute('autoplay', false)
          v.setAttribute('src', srcString)
          v.appendChild(s)

          range.insertNode(p)
          range.insertNode(v)
          range.setStartAfter(p, 0)
        } else {
          this.editor.$txt.append('<audio style="width: 100%" controls src=' + url + data.key + '></audio>')
          this.editor.$txt.append('<p><br></p>')
        }
      }
    },
    insertPic (url) {
      // let selection = window.getSelection()
      let range = window.getSelection().getRangeAt(0)
      console.log(range)
      let d = document.querySelector('#editor')
      let b = document.createElement('br')
      let p = document.createElement('p')
      p.appendChild(b)
      this.duibi(d, range.commonAncestorContainer)
      if (!this.flag) {
        this.$message.warning('请在文本编辑器内选定您想插入文件的位置')
        return false
      }
      let i = document.createElement('img')
      i.setAttribute('src', url)
      i.style.maxWidth = '100%'
      range.insertNode(i)
      range.setStartAfter(i, 0)
    },

    getContent () {
      const content = this.editor.$txt.html()
      if (content.replace('<p><br></p>', '').trim() !== '') {
        return this.editor.$txt.html()
      } else {
        return ''
      }
    },

    seteditor () {
      this.editor = new E('editor')
      this.editor.config.uploadImgShowBase64 = true // base 64 存储图片
      // this.editor.config.uploadImgServer = 'http://otp.cdinfotech.top/file/upload_images' // 配置服务器端地址
      this.editor.config.uploadImgHeaders = { } // 自定义 header
      // this.editor.config.uploadFileName = 'file' // 后端接受上传文件的参数名
      this.editor.config.uploadImgMaxSize = 2 * 1024 * 1024 // 将图片大小限制为 2M
      this.editor.config.uploadImgMaxLength = 1 // 限制一次最多上传 3 张图片
      this.editor.config.uploadImgTimeout = 3 * 60 * 1000 // 设置超时时间
      this.editor.config.zIndex = '0'

      // 配置菜单
      this.editor.config.menus = [
        'source',
        'bold',
        'underline',
        'italic',
        'fontfamily',
        'fontsize',
        'forecolor',
        'bgcolor',
        'alignleft',
        'aligncenter',
        'alignright',
        'link',
        'table',
        'emotion',
        'fullscreen'

        // 'source',
        // 'code',
        // 'head',
        // 'bold',
        // 'fontSize',
        // 'fontName',
        // 'italic',
        // 'underline',
        // 'strikeThrough',
        // 'foreColor',
        // 'backColor',
        // 'link',
        // // 'list',
        // 'justify',
        // // 'quote',
        // 'emoticon',
        // // 'image',
        // 'table',
        // // 'video',
        // // 'undo',
        // // 'redo',
        // 'fullscreen'
      ]

      this.editor.config.uploadImgHooks = {
        customInsert: (insertImg, result, editor) => {
          // 图片上传成功，插入图片的回调
          // let url = 'http://otp.cdinfotech.top/' + result.url
          // insertImg(url)
        }
      }

      this.editor.config.onchange = (html) => {
        this.info_ = html
        this.$emit('change', this.info_)
      }

      // 创建富文本编辑器
      this.editor.create()
    }
  }
}
</script>

<style lang="scss">
  .editor {
    width: 100%;
    margin: 0 auto;
    position: relative;
    z-index: 0;
    margin-bottom: 20px;
  }

  .w-e-toolbar {
    flex-wrap: wrap !important;
    .w-e-droplist{
      top: 0px;
      border: 1px solid #e6e6e6 !important;
      box-shadow: 0px 2px 5px #e6e6e6;
      z-index: 1;
      .w-e-dp-title {
        background: #f1f1f1
      }
      .w-e-list li.w-e-item {
        color: rgb(85, 84, 84) !important;
      }
    }
    .w-e-toolbar .w-e-menu i:hover {
      color: rgb(85, 84, 84) !important;
    }
  }

  .toolbar {
    border: 1px solid #ececec;
    background: #f1f1f1;
  }

  .w-e-text-container .w-e-panel-container {
    border: 1px solid #e6e6e6 !important;
    box-shadow: 0px 2px 5px #e6e6e6;
    input {
      height: 30px !important;
      border-bottom: 1px solid rgb(236, 236, 236) !important;
    }
    .w-e-button-container button {
       color: rgb(85, 84, 84) !important;
    }
  }
  .w-e-text-container img,
  .w-e-text-container video {
    max-width: 100%;
  }

  .w-e-text-container .w-e-panel-container .w-e-panel-tab-content textarea {
    border:1px solid #ececec !important;
  }

  .w-e-toolbar .w-e-droplist ul.w-e-list {
    font-size: 14px;
    color: rgb(85, 84, 84) !important;
    span i {
      display: none;
    }
  }

  .text {
    border: 1px solid #ececec;
    border-top: none;
    text-align: left;
    height: 300px;
  }
</style>
