#Naive Bayes 
#INFORMATION TO RUN:
#command line: python sentiment.py trainingSet.txt testSet.txt

#Imports
from __future__ import division
import sys
import os
import string

#get_Vocab will get each of our vocab words from our data set 
def get_Vocab(data):
	result = set()
	for line in data:
		for word in line:
			result.add(word)

	return sorted(result)

#assign_vocab_prob takes vocab, training, and truth
#we will count our true and false records, and then take the words and determine probabilities of positive or negatives
def assign_vocab_prob(vocab, training, truth):

	#define true or false records
	true_records = 0
	false_records = 0
	
	for value in truth:
		if value == 1:
			true_records += 1
		else:
			false_records += 1

	result = {}
	
	#
	for index, entry in enumerate(vocab):

		true_true_word_count = 0
		false_true_word_count = 0

		false_false_word_count = 0
		true_false_word_count = 0

		for sentence in training:

			if sentence[index] == 1:
				if sentence[-1] == 1:
					true_true_word_count += 1
				else:
					true_false_word_count += 1
			else:
				if sentence[-1] == 1:
					false_true_word_count += 1
				else:
					false_false_word_count += 1

		#Calculate our probabilities
		probabilities = (true_true_word_count/true_records, false_true_word_count/true_records, true_false_word_count/false_records, false_false_word_count/false_records)

		result[entry] = probabilities
	#we will return our result, and compare true and false records
	return result, (true_records / len(truth), false_records / len(truth))

#Test
#We will recieve sentences, our trained vocab, provababilities, and vocab
def test_Phase(sentences, trained_vocab, probabilities, vocab):
	
	#define results array
	result = []
	
	#set prob class true equal to our 0 vals
	prob_class_true = probabilities[0]
	#set prob class true equal to our 1 vals
	prob_class_false = probabilities[1]
	#For each sentence, we will exercise our checks of probability of truth and probability of false
	for sentence in sentences:
		prob_true = prob_class_true
		for index, word in enumerate(sentence):
			if vocab[index] in trained_vocab:
				if word == 1:
					prob_true *= trained_vocab[vocab[index]][0]
				else:
					prob_true *= trained_vocab[vocab[index]][1]

		prob_false = prob_class_false
		for index, word in enumerate(sentence):
			if vocab[index] in trained_vocab:
				if word == 1:
					prob_false *= trained_vocab[vocab[index]][2]
				else:
					prob_false *= trained_vocab[vocab[index]][3]

		if prob_true >= prob_false:
			result.append(1)
		else:
			result.append(0)

	return result

	#check_accuracy will check how accurate our approach was
def check_accuracy(calculated_truth, real_truth):
	#number correct
	correct = 0
	#iterate through, and check for correctness
	for i in range(len(real_truth)):
		if calculated_truth[i] == real_truth[i]:
			correct += 1

	return correct / len(real_truth)

def get_Data(fileName):
	#array for our data
	data = []
	#array for true data
	dataTruth = []
	#deleteChars will delete unneeded punctuation and numbers
	deleteChars = string.punctuation + "1234567890"
	with open(fileName) as f:
		training = f.readlines()

	for line in training:
		temp = line.split('\t')

		data.append(temp[0].translate(None, deleteChars).lower().split())
		dataTruth.append(temp[1].strip())

	dataTruth = list(map(int, dataTruth))
	return data, dataTruth

def build_Feature_Vector(vocab, sentences, truth):
	result = []

	truthIndex = 0

	for sentence in sentences:
		#feature vector of size M+1, where M is the size of the vocab
		temp = [0 for _ in range(len(vocab))]
		#for each word in sentence, we will place int into an index
		for word in sentence:
			if word in vocab:
				try:
					index = vocab.index(word)
				except:
					continue

			temp[index] = 1

		temp[len(vocab) - 1] = truth[truthIndex]
		result.append(temp)
		truthIndex += 1

	return result

	#outputPreprocess
	#we will output our info to our file
def outputPreprocess(vocab, train, test):
#remove files if they exist already
	os.system('rm preprocessed_train.txt')
	os.system('rm preprocessed_test.txt')
#create and write to test and train file
	trainFile = open("preprocessed_train.txt", 'w')
	testFile = open("preprocessed_test.txt", 'w')
#wrire our vocab
	for word in vocab:
		trainFile.write(word + ",")
		testFile.write(word + ",")

	trainFile.write("classlabel\n")
	testFile.write("classlabel\n")
#write our training 
	for feature in train:
		for idx, i in enumerate(feature):
			if idx == len(feature) - 1:
				trainFile.write(str(i))
			else:
				trainFile.write(str(i) + ",")
		trainFile.write("\n")
#write our testing
	for feature in test:
		for idx, i in enumerate(feature):
			if idx == len(feature) - 1:
				testFile.write(str(i))
			else:
				testFile.write(str(i) + ",")
		testFile.write("\n")

		
#main
#call and recieve all of our methods
#
def main(args):
	#Define our arrays to pass between methods
	#Vocab array
	vocab = []
	#trainingWords array
	trainingWords = []
	#testingWords array
	testingWords = []
	#trainingTruth array
	trainingTruth = []
	#testingTruth array
	testingTruth = []
	#training array
	training = []
	#testing array
	testing = []

	
	#get our training data, and true info
	trainingSentences, trainingTruth = get_Data(args[1])

	#get our testing data, and Test info
	testingSentences, testingTruth = get_Data(args[2])

	#Get our list of vocabulary
	vocab = get_Vocab(trainingSentences)

	#build our training vector
	training = build_Feature_Vector(vocab, trainingSentences, trainingTruth)

	#build our testing vector
	testing = build_Feature_Vector(vocab, testingSentences, testingTruth)

	#output our preprocessed data
	outputPreprocess(vocab, training, testing)

	#place our assigned vocab provababilities into our trained vocab
	trained_vocab, (p_class_1, p_class_0) = assign_vocab_prob(vocab, training, trainingTruth)

	#test phase to our training classification
	training_classification = test_Phase(training, trained_vocab, (p_class_1, p_class_0), vocab)
	
	#test phase to our test classification
	testing_classification = test_Phase(testing, trained_vocab, (p_class_1, p_class_0), vocab)

	#check our accuracy of our training classifications to our training truths
	result1 = check_accuracy(training_classification, trainingTruth)
	#check our accuracy of our testing classifications to our testing truths
	result2 = check_accuracy(testing_classification, testingTruth)

	#output our results to results.txt
	output = open('results.txt', 'w')
	output.write("Training Set (training compared to training): ")
	output.write(str(result1) + '\n')
	output.write("Testing Set (training training compared to testing): ")
	output.write(str(result2) + '\n')


if __name__ == "__main__":
	main(sys.argv)
