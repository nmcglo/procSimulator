
class ProjectTimings 
	def initialize()
		@timingMap ={}
		@list
	end
	def createDefault()
		@timingMap = {idle: 0, userWait:0, iOWait:0, active: 0,
    terminated: 0, cTXSwitch:0}
	end
	def createCustom(strList)
		strList.each {|st| @timingMap[st] = 0}
	end
	def createCustomInt
		$tList.each {|st| @timingMap[st] = 0}
	end

	
	def tickTime(tpi)
		@timingMap[tpi] += 1
	end
	def getTiming(tpi)
		@timingMap[tpi]
	end
end

ProjectTimings.new()