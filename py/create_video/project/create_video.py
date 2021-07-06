import sys
import os
sys.path.append(os.getcwd()+"/py/create_video/lib/")
from pydub import AudioSegment
from moviepy.editor import concatenate_videoclips, CompositeAudioClip, VideoFileClip, AudioFileClip
import cv2



#将图片转换为视频
def video(path):
    now_path = os.getcwd()
    back_pics = os.listdir(now_path+'\\py\\create_video\\project\\input\\back\\')
    fileNames = os.listdir(path)
    # 读取时序图中的第一张图片
    img = cv2.imread(path + fileNames[0])
    # 设置每秒读取多少张图片
    fps = 0.5
    imgInfo = img.shape

    # 获取图片宽高度信息
    size = (imgInfo[1], imgInfo[0])
    fourcc = cv2.VideoWriter_fourcc(*"MJPG")

    # 定义写入图片的策略
    videoWrite = cv2.VideoWriter(now_path+'\py\create_video\project/video/output.mp4', fourcc, fps, size)

    for fileName in fileNames:
        # 读取所有的图片
        img = cv2.imread(path + '/' + fileName)
        img = cv2.resize(img, size)
        # 将图片写入所创建的视频对象
        videoWrite.write(img)
    
    for back_pic in back_pics:#背景图片
        img = cv2.imread(now_path+'\py\create_video\project/input/back/'+back_pic)
        img = cv2.resize(img,size)
        videoWrite.write(img)
    videoWrite.release()

#添加音频文件
def create_video(videoFile):
    
    video = VideoFileClip(videoFile)  # 读入视频文件
    t = int(video.duration)
    audioFile = os.getcwd()+'\py\create_video\project/audio/backgrund_music.mp3'#音频目录
    new_audioFile = os.getcwd()+'\py\create_video\project/audio/new_backgrund_music.mp3'#存放剪切的音频文件
    audio = AudioFileClip(audioFile)#音频
    audio1 = audio.subclip(3,t+4)

    audio = AudioFileClip(new_audioFile)#音频

    video = video.set_audio(audio1)

    video.write_videofile(os.getcwd()+'\py\create_video\project/video/with_audio.mp4',audio_codec='aac')#第一个参数为保存的位置
    
   
def app_fun():
    now_path = os.getcwd()
    print(now_path)
    videoFile = now_path+'\py\create_video\project/video/output.mp4'
    path = now_path+'\py\create_video\project/input/img/'
    video(path)

    create_video(videoFile)

if __name__=='__main__':
    app_fun()
